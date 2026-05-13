package pt.pauloortolan.springairagexample.exceptions;

public class DocumentConversionException extends RuntimeException {
    public DocumentConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
