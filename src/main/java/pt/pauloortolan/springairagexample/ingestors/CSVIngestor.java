package pt.pauloortolan.springairagexample.ingestors;

import de.siegmar.fastcsv.reader.NamedCsvRecord;
import pt.pauloortolan.springairagexample.exceptions.DocumentLoadingException;

public interface CSVIngestor<E> {

    E ingest(NamedCsvRecord namedRecord) throws DocumentLoadingException;

}
