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
import pt.pauloortolan.springairagexample.persistence.OscarNominee;
import pt.pauloortolan.springairagexample.persistence.OscarRepository;

@Slf4j
@Service
public class OscarIngestorService extends BaseIngestorService<OscarNominee> {

    private final OscarRepository repository;
    @Qualifier("oscarIngestor")
    private final CSVIngestor<OscarNominee> ingestor;

    @Value("${app.oscars.file}")
    private Resource oscarNomineeFile;

    public OscarIngestorService(
            OscarRepository repository,
            CSVIngestor<OscarNominee> ingestor,
            VectorStore vectorStore,
            ObjectMapper objectMapper) {
        super(vectorStore, objectMapper);
        this.repository = repository;
        this.ingestor = ingestor;
    }

    @Override
    protected Resource getFile() {
        log.info("OscarIngestorService::getFile()");
        return oscarNomineeFile;
    }

    @Override
    protected OscarNominee parseRecord(NamedCsvRecord namedRecord) throws DocumentLoadingException {
        log.info("OscarIngestorService::parseRecord((namedRecord={}))", namedRecord);
        return ingestor.ingest(namedRecord);
    }

    @Override
    protected Document toDocument(OscarNominee oscarNominee) {
        log.info("OscarIngestorService::toDocument((oscarNominee={}))", oscarNominee);
        try {
            return new Document(getObjectMapper().writeValueAsString(oscarNominee), oscarNominee.toMetadata());
        } catch (JsonProcessingException e) {
            throw new DocumentConversionException(e.getMessage(), e);
        }
    }

    @Override
    protected OscarNominee save(OscarNominee entity) {
        log.info("OscarIngestorService::save(OscarNominee)");
        return repository.save(entity);
    }
}
