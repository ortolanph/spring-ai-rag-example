package pt.pauloortolan.springairagexample.exceptions;

import lombok.Getter;

@Getter
public class DocumentLoadingException extends Exception {

    private final String failedDocument;

    public DocumentLoadingException(String message, String failedDocument) {
        super(message);
        this.failedDocument = failedDocument;
    }

}
