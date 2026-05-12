package pt.pauloortolan.springairagexample.pojo;

import com.fasterxml.jackson.annotation.*;

import java.time.*;
import java.util.HashSet;
import java.util.Set;

public record LoadStatistics(
    Integer loaded,
    Integer failed,
    Integer total,
    Set<FailedDocument> failedDocuments,
    @JsonIgnore Instant start,
    @JsonIgnore Instant end) {

    public LoadStatistics {
        failedDocuments = Set.copyOf(failedDocuments);
    }

    public static LoadStatistics reset() {
        return new LoadStatistics(0, 0, 0, new HashSet<>(), Instant.now(), null);
    }

    public LoadStatistics addLoaded() {
        return new LoadStatistics(loaded + 1, failed, total + 1, failedDocuments, start, null);
    }

    public LoadStatistics addFailed(String documentId, String message) {
        Set<FailedDocument> updated = new HashSet<>(failedDocuments);
        updated.add(new FailedDocument(documentId, message));
        return new LoadStatistics(loaded, failed + 1, total + 1, updated, start, null);
    }

    public LoadStatistics finish() {
        return new LoadStatistics(loaded, failed, total, failedDocuments, start, Instant.now());
    }

    @JsonProperty("duration")
    public String duration() {
        if (start == null || end == null) return "N/A";
        Duration d = Duration.between(start, end);
        return String.format("%d:%02d:%02d", d.toHours(), d.toMinutesPart(), d.toSecondsPart());
    }

}
