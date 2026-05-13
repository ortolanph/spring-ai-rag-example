package pt.pauloortolan.springairagexample.ingestors;

import de.siegmar.fastcsv.reader.*;
import pt.pauloortolan.springairagexample.exceptions.*;

public interface CSVIngestor<E> {

    E ingest(NamedCsvRecord namedRecord) throws DocumentLoadingException;

}
