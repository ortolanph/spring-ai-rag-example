package pt.pauloortolan.springairagexample.pojo;

import java.util.HashSet;
import java.util.Set;

public record LoadStatistics(Integer loaded, Integer failed, Integer total, Set<FailedDocument> failedDocuments) {

    public static LoadStatistics reset() {
        return new LoadStatistics(0, 0, 0, new HashSet<>());
    }

    public LoadStatistics addLoaded() {
        return new LoadStatistics(loaded() + 1, failed(), total() + 1, failedDocuments);
    }

    public LoadStatistics addFailed(String failedDocument, String message) {
        Set<FailedDocument> failedDocuments = failedDocuments();
        failedDocuments.add(new FailedDocument(failedDocument, message));

        return new LoadStatistics(loaded(), failed() + 1, total() + 1, failedDocuments);
    }
}
