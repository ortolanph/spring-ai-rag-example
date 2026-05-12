package pt.pauloortolan.springairagexample.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.NamedCsvRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pt.pauloortolan.springairagexample.exceptions.DocumentLoadingException;
import pt.pauloortolan.springairagexample.exceptions.IngestorException;
import pt.pauloortolan.springairagexample.persistence.Climate;
import pt.pauloortolan.springairagexample.persistence.ClimateRepository;
import pt.pauloortolan.springairagexample.pojo.LoadStatistics;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClimateIngestorService {

    private final ClimateRepository repository;
    private final VectorStore vectorStore;
    private final ObjectMapper objectMapper;

    @Value("${app.climate.file}")
    private Resource dataFile;

    public LoadStatistics ingestData() throws IngestorException {
        log.info("ClimateIngestorService::ingestData()");
        LoadStatistics statistics = LoadStatistics.reset();
        List<Document> documents = new ArrayList<>();

        try {
            Path csvRawData = dataFile.getFile().toPath();

            CsvReader<NamedCsvRecord> csvReader = CsvReader
                    .builder()
                    .ofNamedCsvRecord(csvRawData, StandardCharsets.UTF_8);

            for (NamedCsvRecord namedRecord : csvReader) {
                try {
                    Climate climateData = ingestDocument(namedRecord);

                    repository.save(climateData);
                    documents.add(
                            new Document(
                                    objectMapper.writeValueAsString(climateData),
                                    climateData.toMetadata()
                            )
                    );
                    statistics = statistics.addLoaded();
                } catch (DocumentLoadingException dlException) {
                    log.error("Failed to load OscarNominee data", dlException);
                    statistics = statistics.addFailed(dlException.getFailedDocument(), dlException.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IngestorException(e.getMessage());
        }

        vectorStore.add(documents);

        return statistics;
    }

    private Climate ingestDocument(NamedCsvRecord namedRecord) throws DocumentLoadingException {
        try {
            return new Climate(
                    UUID.randomUUID(),
                    namedRecord.getField("time"),
                    Double.parseDouble(namedRecord.getField("temperature")),
                    Double.parseDouble(namedRecord.getField("rain")),
                    Double.parseDouble(namedRecord.getField("cloud_cover")),
                    Double.parseDouble(namedRecord.getField("relative_humidity")));
        } catch (Exception e) {
            throw new DocumentLoadingException(namedRecord.toString(), e.getMessage());
        }
    }
}
