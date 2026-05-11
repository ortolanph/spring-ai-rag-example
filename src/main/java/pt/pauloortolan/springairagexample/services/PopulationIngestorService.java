package pt.pauloortolan.springairagexample.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.NamedCsvRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pt.pauloortolan.springairagexample.exceptions.DocumentLoadingException;
import pt.pauloortolan.springairagexample.exceptions.PopulationIngestionException;
import pt.pauloortolan.springairagexample.persistence.Population;
import pt.pauloortolan.springairagexample.persistence.PopulationRepository;
import pt.pauloortolan.springairagexample.pojo.LoadStatistics;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PopulationIngestorService {

    private final PopulationRepository populationRepository;
    private final VectorStore vectorStore;

    private ObjectMapper objectMapper;

    @Value("${app.population.file}")
    private String populationFile;

    public LoadStatistics loadPopulationData() throws PopulationIngestionException {
        LoadStatistics statistics = LoadStatistics.reset();
        List<Document> documents = new ArrayList<>();

        try {
            String data = Files
                    .readString(new java.io.File(populationFile).toPath());

            CsvReader<NamedCsvRecord> csvReader = CsvReader.builder().ofNamedCsvRecord(data);

            for (NamedCsvRecord namedRecord : csvReader) {
                parseRecord(namedRecord, documents, statistics);
            }
        } catch (IOException e) {
            throw new PopulationIngestionException(e.getMessage());
        }

        vectorStore.add(documents);

        return statistics;
    }

    private void parseRecord(NamedCsvRecord csvRecord, List<Document> documents, LoadStatistics statistics) throws JsonProcessingException {
        try {
            Population population = ingest(csvRecord);

            populationRepository.save(population);
            documents.add(
                    new Document(
                            objectMapper.writeValueAsString(population),
                            population.toMetadata()
                    )
            );

            statistics.addLoaded();
        } catch (DocumentLoadingException dliException) {
            statistics.addFailed(dliException.getFailedDocument(), dliException.getMessage());
        }
    }

    private Population ingest(NamedCsvRecord populationRecord) throws DocumentLoadingException {
        try {
            return new Population(
                    UUID.randomUUID(),
                    populationRecord.getField("Rank").isEmpty() ? null : Integer.parseInt(populationRecord.getField("Rank")),
                    populationRecord.getField("CCA3"),
                    populationRecord.getField("Country/Territory"),
                    populationRecord.getField("Capital"),
                    populationRecord.getField("Continent"),
                    populationRecord.getField("2022 Population").isEmpty() ? null : Long.parseLong(populationRecord.getField("2022 Population")),
                    populationRecord.getField("2020 Population").isEmpty() ? null : Long.parseLong(populationRecord.getField("2020 Population")),
                    populationRecord.getField("2015 Population").isEmpty() ? null : Long.parseLong(populationRecord.getField("2015 Population")),
                    populationRecord.getField("2010 Population").isEmpty() ? null : Long.parseLong(populationRecord.getField("2010 Population")),
                    populationRecord.getField("2000 Population").isEmpty() ? null : Long.parseLong(populationRecord.getField("2000 Population")),
                    populationRecord.getField("1990 Population").isEmpty() ? null : Long.parseLong(populationRecord.getField("1990 Population")),
                    populationRecord.getField("1980 Population").isEmpty() ? null : Long.parseLong(populationRecord.getField("1980 Population")),
                    populationRecord.getField("1970 Population").isEmpty() ? null : Long.parseLong(populationRecord.getField("1970 Population")),
                    populationRecord.getField("Area (km²)").isEmpty() ? null : Long.parseLong(populationRecord.getField("Area (km²)")),
                    populationRecord.getField("Density (per km²)").isEmpty() ? null : Double.parseDouble(populationRecord.getField("Density (per km²)")),
                    populationRecord.getField("Growth Rate").isEmpty() ? null : Double.parseDouble(populationRecord.getField("Growth Rate")),
                    populationRecord.getField("World Population Percentage").isEmpty() ? null : Double.parseDouble(populationRecord.getField("World Population Percentage")));
        } catch (Exception e) {
            throw new DocumentLoadingException(populationRecord.toString(), e.getMessage());
        }
    }
}

