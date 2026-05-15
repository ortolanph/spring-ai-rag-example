package pt.pauloortolan.springairagexample.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.pauloortolan.springairagexample.pojo.PopulationCountResponse;
import pt.pauloortolan.springairagexample.services.PopulationService;

@Slf4j
@RestController
@RequestMapping("/populations")
@RequiredArgsConstructor
public class PopulationController {

    private final PopulationService populationService;

    @GetMapping("/topTenMostPopulated")
    public PopulationCountResponse getMostPopulatedCountries() {
        return populationService.getMostPopultatedCountries();
    }

    @GetMapping("/topFiveMostPopulatedInContinent/{continent}")
    public PopulationCountResponse getTopFivePopulatedCountriesByContinent(@PathVariable String continent) {
        return populationService.getTopFivePopulatedCountriesByContinent(continent);
    }

}
