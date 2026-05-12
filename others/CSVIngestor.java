package pt.pauloortolan.springairagexample.services;

import de.siegmar.fastcsv.reader.*;
import pt.pauloortolan.springairagexample.exceptions.*;

public interface CSVIngestor<E> {

    E ingest(NamedCsvRecord csvRecord) throws DocumentLoadingException;

}
