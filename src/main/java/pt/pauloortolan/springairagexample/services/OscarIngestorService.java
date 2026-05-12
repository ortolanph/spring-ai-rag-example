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
import pt.pauloortolan.springairagexample.exceptions.OscarNomineesException;
import pt.pauloortolan.springairagexample.persistence.OscarNominee;
import pt.pauloortolan.springairagexample.persistence.OscarRepository;
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
public class OscarIngestorService {

    private final OscarRepository repository;
    private final VectorStore vectorStore;
    private final ObjectMapper objectMapper;

    @Value("${app.oscars.file}")
    private Resource oscarNomineeFile;

    public LoadStatistics loadOscarNomineeData() throws OscarNomineesException {
        log.info("OscarIngestorService::loadOscarNomineeData()");
        LoadStatistics statistics = LoadStatistics.reset();
        List<Document> documents = new ArrayList<>();

        try {
            Path oscarData = oscarNomineeFile.getFile().toPath();

            CsvReader<NamedCsvRecord> csvReader = CsvReader
                    .builder()
                    .ofNamedCsvRecord(oscarData, StandardCharsets.UTF_8);

            for (NamedCsvRecord namedRecord : csvReader) {
                try {
                    OscarNominee nominee = ingest(namedRecord);

                    repository.save(nominee);
                    documents.add(
                            new Document(
                                    objectMapper.writeValueAsString(nominee),
                                    nominee.toMetadata()
                            )
                    );
                    statistics = statistics.addLoaded();
                } catch (DocumentLoadingException dlException) {
                    log.error("Failed to load OscarNominee data", dlException);
                    statistics = statistics.addFailed(dlException.getFailedDocument(), dlException.getMessage());
                }
            }
        } catch (IOException e) {
            throw new OscarNomineesException(e.getMessage());
        }

        vectorStore.add(documents);

        return statistics;
    }

    private OscarNominee ingest(NamedCsvRecord namedRecord) throws DocumentLoadingException {
        try {
            return OscarNominee.builder()
                    .id(UUID.randomUUID())
                    .yearCeremony(Integer.parseInt(namedRecord.getField("year_ceremony")))
                    .yearFilm(namedRecord.getField("year_film"))
                    .ceremony(Integer.parseInt(namedRecord.getField("ceremony")))
                    .category(namedRecord.getField("category"))
                    .categoryName(namedRecord.getField("canon_category"))
                    .name(namedRecord.getField("name"))
                    .film(namedRecord.getField("film"))
                    .winner(Boolean.parseBoolean(namedRecord.getField("winner")))
                    .build();
        } catch (Exception e) {
            throw new DocumentLoadingException(namedRecord.toString(), e.getMessage());
        }
    }
}
