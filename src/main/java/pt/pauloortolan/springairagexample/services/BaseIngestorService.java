package pt.pauloortolan.springairagexample.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.NamedCsvRecord;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pt.pauloortolan.springairagexample.exceptions.DocumentLoadingException;
import pt.pauloortolan.springairagexample.exceptions.IngestionException;
import pt.pauloortolan.springairagexample.pojo.LoadStatistics;

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

    public final LoadStatistics loadData() throws IngestionException {
        log.info("BaseIngestorService::loadData()");
        LoadStatistics statistics = LoadStatistics.reset();
        List<Document> documents = new ArrayList<>();

        try {
            Path file = getFile().getFile().toPath();
            CsvReader<NamedCsvRecord> csvReader = CsvReader
                    .builder()
                    .ofNamedCsvRecord(file, StandardCharsets.UTF_8);

            for (NamedCsvRecord namedRecord : csvReader) {
                try {
                    T entity = parseRecord(namedRecord);
                    save(entity);
                    documents.add(toDocument(entity));
                    statistics = statistics.addLoaded();
                } catch (DocumentLoadingException e) {
                    statistics = statistics.addFailed(e.getFailedDocument(), e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IngestionException(e.getMessage());
        }

        vectorStore.add(documents);
        return statistics.finish();
    }

    // --- Hook methods (subclasses must implement) ---
    protected abstract Resource getFile();

    protected abstract T parseRecord(NamedCsvRecord namedRecord) throws DocumentLoadingException;

    protected abstract Document toDocument(T entity);

    protected abstract void save(T entity);
}