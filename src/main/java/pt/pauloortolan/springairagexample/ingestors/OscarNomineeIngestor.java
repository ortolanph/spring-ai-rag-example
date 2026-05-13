package pt.pauloortolan.springairagexample.ingestors;

import de.siegmar.fastcsv.reader.NamedCsvRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pt.pauloortolan.springairagexample.exceptions.DocumentLoadingException;
import pt.pauloortolan.springairagexample.persistence.OscarNominee;

import java.util.UUID;

@Slf4j
@Component
@Qualifier("oscarIngestor")
public class OscarNomineeIngestor implements CSVIngestor<OscarNominee> {
    @Override
    public OscarNominee ingest(NamedCsvRecord namedRecord) throws DocumentLoadingException {
        log.info("OscarNomineeIngestor::ingest((namedRecord={}))", namedRecord);
        try {
            return new OscarNominee(
                    UUID.randomUUID(),
                    Integer.parseInt(namedRecord.getField("year_ceremony")),
                    namedRecord.getField("year_film"),
                    Integer.parseInt(namedRecord.getField("ceremony")),
                    namedRecord.getField("category"),
                    namedRecord.getField("canon_category"),
                    namedRecord.getField("name"),
                    namedRecord.getField("film"),
                    Boolean.parseBoolean(namedRecord.getField("winner")));
        } catch (Exception e) {
            throw new DocumentLoadingException(namedRecord.toString(), e.getMessage());
        }
    }
}
