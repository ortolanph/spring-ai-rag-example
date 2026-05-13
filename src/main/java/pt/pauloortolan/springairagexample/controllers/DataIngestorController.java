package pt.pauloortolan.springairagexample.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.pauloortolan.springairagexample.exceptions.IngestionException;
import pt.pauloortolan.springairagexample.pojo.LoadStatistics;
import pt.pauloortolan.springairagexample.services.ClimateIngestorService;
import pt.pauloortolan.springairagexample.services.OscarIngestorService;
import pt.pauloortolan.springairagexample.services.PopulationIngestorService;

@Slf4j
@RestController
@RequestMapping("/ingest")
@RequiredArgsConstructor
public class DataIngestorController {

    private final PopulationIngestorService populationService;
    private final OscarIngestorService oscarNomineeService;
    private final ClimateIngestorService climateService;

    @PostMapping("/climate")
    public ResponseEntity<LoadStatistics> loadClimateData() throws IngestionException {
        LoadStatistics stats = climateService.loadData();

        return ResponseEntity.ok(stats);
    }

    @PostMapping("/population")
    public ResponseEntity<LoadStatistics> loadPopulationData() throws IngestionException {
        log.info("PopulationIngestorService::loadPopulationData()");
        LoadStatistics stats = populationService.loadData();

        return ResponseEntity.ok(stats);
    }

    @PostMapping("/oscars")
    public ResponseEntity<LoadStatistics> loadOscarsData() throws IngestionException {
        LoadStatistics stats = oscarNomineeService.loadData();

        return ResponseEntity.ok(stats);
    }

}
