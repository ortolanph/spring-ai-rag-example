package pt.pauloortolan.springairagexample.ingestors;

import de.siegmar.fastcsv.reader.NamedCsvRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pt.pauloortolan.springairagexample.exceptions.DocumentLoadingException;
import pt.pauloortolan.springairagexample.persistence.Population;

import java.util.UUID;

@Slf4j
@Component
@Qualifier("populatorIngestor")
public class PopulatorIngestor implements CSVIngestor<Population> {

    @Override
    public Population ingest(NamedCsvRecord namedRecord) throws DocumentLoadingException {
        log.info("PopulatorIngestor::ingest((namedRecord={}))", namedRecord);
        try {
            return new Population(
                    UUID.randomUUID(),
                    namedRecord.getField("Rank").isEmpty() ? null : Integer.parseInt(namedRecord.getField("Rank")),
                    namedRecord.getField("CCA3"),
                    namedRecord.getField("Country/Territory"),
                    namedRecord.getField("Capital"),
                    namedRecord.getField("Continent"),
                    namedRecord.getField("2022 Population").isEmpty() ? null : Long.parseLong(namedRecord.getField("2022 Population")),
                    namedRecord.getField("2020 Population").isEmpty() ? null : Long.parseLong(namedRecord.getField("2020 Population")),
                    namedRecord.getField("2015 Population").isEmpty() ? null : Long.parseLong(namedRecord.getField("2015 Population")),
                    namedRecord.getField("2010 Population").isEmpty() ? null : Long.parseLong(namedRecord.getField("2010 Population")),
                    namedRecord.getField("2000 Population").isEmpty() ? null : Long.parseLong(namedRecord.getField("2000 Population")),
                    namedRecord.getField("1990 Population").isEmpty() ? null : Long.parseLong(namedRecord.getField("1990 Population")),
                    namedRecord.getField("1980 Population").isEmpty() ? null : Long.parseLong(namedRecord.getField("1980 Population")),
                    namedRecord.getField("1970 Population").isEmpty() ? null : Long.parseLong(namedRecord.getField("1970 Population")),
                    namedRecord.getField("Area (km²)").isEmpty() ? null : Long.parseLong(namedRecord.getField("Area (km²)")),
                    namedRecord.getField("Density (per km²)").isEmpty() ? null : Double.parseDouble(namedRecord.getField("Density (per km²)")),
                    namedRecord.getField("Growth Rate").isEmpty() ? null : Double.parseDouble(namedRecord.getField("Growth Rate")),
                    namedRecord.getField("World Population Percentage").isEmpty() ? null : Double.parseDouble(namedRecord.getField("World Population Percentage")));
        } catch (Exception e) {
            throw new DocumentLoadingException(e.getMessage(), namedRecord.toString());
        }
    }

}
