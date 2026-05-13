package pt.pauloortolan.springairagexample.ingestors;

import de.siegmar.fastcsv.reader.NamedCsvRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pt.pauloortolan.springairagexample.exceptions.DocumentLoadingException;
import pt.pauloortolan.springairagexample.persistence.Climate;

import java.util.UUID;

@Slf4j
@Component
@Qualifier("climateIngestor")
public class ClimateIngestor implements CSVIngestor<Climate> {

    @Override
    public Climate ingest(NamedCsvRecord namedRecord) throws DocumentLoadingException {
        log.info("ClimateIngestor::ingest((namedRecord={}))", namedRecord);
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
