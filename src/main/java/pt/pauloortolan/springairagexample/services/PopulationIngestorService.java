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
import pt.pauloortolan.springairagexample.exceptions.DocumentLoadingException;
import pt.pauloortolan.springairagexample.ingestors.CSVIngestor;
import pt.pauloortolan.springairagexample.persistence.Population;
import pt.pauloortolan.springairagexample.persistence.PopulationRepository;

import java.util.UUID;

@Slf4j
@Service
public class PopulationIngestorService extends BaseIngestorService<Population> {

    private final PopulationRepository populationRepository;
    @Qualifier("populatorIngestor")
    private final CSVIngestor<Population> populatorIngestor;

    @Value("${app.population.file}")
    private Resource populationFile;

    public PopulationIngestorService(
            CSVIngestor<Population> populationFileIngestor,
            PopulationRepository populationRepository,
            VectorStore vectorStore,
            ObjectMapper objectMapper) {
        log.info("PopulationIngestorService::constructor()");
        this.populationRepository = populationRepository;
        this.populatorIngestor = populationFileIngestor;
        super(vectorStore, objectMapper);
    }

    @Override
    protected Resource getFile() {
        log.info("PopulationIngestorService::getFile()");
        return populationFile;
    }

    @Override
    protected void save(Population population) {
        log.info("PopulationIngestorService::save((population={}))", population);
        populationRepository.save(population);
    }

    @Override
    protected Document toDocument(Population population) {
        log.info("PopulationIngestorService::toDocument((population={}))", population);
        try {
            return new Document(getObjectMapper().writeValueAsString(population), population.toMetadata());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Population parseRecord(NamedCsvRecord namedRecord) throws DocumentLoadingException {
        log.info("PopulationIngestorService::parseRecord((namedRecord={}))", namedRecord);
        return populatorIngestor.ingest(namedRecord);
    }
}