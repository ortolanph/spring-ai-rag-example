package pt.pauloortolan.springairagexample.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.siegmar.fastcsv.reader.NamedCsvRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pt.pauloortolan.springairagexample.exceptions.DocumentConversionException;
import pt.pauloortolan.springairagexample.exceptions.DocumentLoadingException;
import pt.pauloortolan.springairagexample.ingestors.CSVIngestor;
import pt.pauloortolan.springairagexample.persistence.Climate;
import pt.pauloortolan.springairagexample.persistence.ClimateRepository;

@Slf4j
@Service
public class ClimateIngestorService extends BaseIngestorService<Climate> {

    private final ClimateRepository repository;
    @Qualifier("climateIngestor")
    private final CSVIngestor<Climate> ingestor;
    @Value("${app.climate.file}")
    private Resource climateFile;

    public ClimateIngestorService(
            ClimateRepository repository,
            CSVIngestor<Climate> ingestor,
            VectorStore vectorStore,
            ObjectMapper objectMapper) {
        log.info("ClimateIngestorService::constructor()");
        super(vectorStore, objectMapper);
        this.repository = repository;
        this.ingestor = ingestor;
    }

    @Override
    protected Resource getFile() {
        log.info("ClimateIngestorService::getFile()");
        return climateFile;
    }

    @Override
    protected Climate parseRecord(NamedCsvRecord namedRecord) throws DocumentLoadingException {
        log.info("ClimateIngestorService::parseRecord((namedRecord={}))", namedRecord);
        return ingestor.ingest(namedRecord);
    }

    @Override
    protected Document toDocument(Climate climate) {
        log.info("ClimateIngestorService::toDocument((climate={}))", climate);
        try {
            return new Document(getObjectMapper().writeValueAsString(climate), climate.toMetadata());
        } catch (JsonProcessingException e) {
            throw new DocumentConversionException(e.getMessage(), e);
        }
    }

    @Override
    protected void save(Climate climate) {
        log.info("ClimateIngestorService::save(climate={})", climate);
        repository.save(climate);
    }
}
