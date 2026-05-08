package pt.pauloortolan.springairagexample.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.pauloortolan.springairagexample.exceptions.PopulationIngestionException;
import pt.pauloortolan.springairagexample.pojo.LoadStatistics;
import pt.pauloortolan.springairagexample.services.PopulationIngestorService;

import java.util.Set;

@RestController
@RequestMapping("/ingest")
@RequiredArgsConstructor
public class DataIngestorController {

    private final PopulationIngestorService service;

    @PostMapping("/climate")
    public ResponseEntity<LoadStatistics> loadClimateData() {
        return ResponseEntity.ok(new LoadStatistics(1000, 0, 1000, Set.of()));
    }

    @PostMapping("/population")
    public ResponseEntity<LoadStatistics> loadPopulationData() throws PopulationIngestionException {
        LoadStatistics stats = service.loadPopulationData();

        return ResponseEntity.ok(stats);
    }

    @PostMapping("/oscars")
    public ResponseEntity<LoadStatistics> loadOscarsData() {
        return ResponseEntity.ok(new LoadStatistics(1000, 0, 1000, Set.of()));
    }

}
