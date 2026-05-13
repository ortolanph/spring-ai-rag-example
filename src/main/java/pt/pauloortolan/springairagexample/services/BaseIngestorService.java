package pt.pauloortolan.springairagexample.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.NamedCsvRecord;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pt.pauloortolan.springairagexample.exceptions.DocumentLoadingException;
import pt.pauloortolan.springairagexample.exceptions.IngestionException;
import pt.pauloortolan.springairagexample.pojo.LoadStatistics;
import pt.pauloortolan.springairagexample.pojo.PartitionedList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class BaseIngestorService<T> {

    private final VectorStore vectorStore;
    @Getter
    private final ObjectMapper objectMapper;

    @Value("${app.batch-size}")
    private int batchSize;

    public final LoadStatistics loadData() throws IngestionException {
        log.info("BaseIngestorService::loadData()");
        LoadStatistics statistics = LoadStatistics.reset();

        try {
            Path file = getFile().getFile().toPath();
            CsvReader<NamedCsvRecord> csvReader = CsvReader
                    .builder()
                    .ofNamedCsvRecord(file, StandardCharsets.UTF_8);

            PartitionedList<NamedCsvRecord> partitionedRecords =
                    new PartitionedList<>(csvReader.stream().toList(), batchSize);

            int interaction = 1;

            while (!partitionedRecords.isEmpty()) {
                log.info("Load interaction {} of {} records", interaction, partitionedRecords.size());
                List<NamedCsvRecord> records = partitionedRecords.head();

                List<Document> documents = new ArrayList<>();

                for (NamedCsvRecord namedRecord : records) {
                    try {
                        T entity = parseRecord(namedRecord);
                        save(entity);
                        documents.add(toDocument(entity));
                        statistics = statistics.addLoaded();
                    } catch (DocumentLoadingException e) {
                        statistics = statistics.addFailed(e.getFailedDocument(), e.getMessage());
                    }
                }

                vectorStore.add(documents);
                log.info("Interaction {} completed", interaction);
                interaction++;
                partitionedRecords = partitionedRecords.tail();
            }

        } catch (IOException e) {
            throw new IngestionException(e.getMessage());
        }

        return statistics.finish();
    }

    // --- Hook methods (subclasses must implement) ---
    protected abstract Resource getFile();

    protected abstract T parseRecord(NamedCsvRecord namedRecord) throws DocumentLoadingException;

    protected abstract Document toDocument(T entity);

    protected abstract T save(T entity);
}