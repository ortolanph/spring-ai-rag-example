package pt.pauloortolan.springairagexample.persistence;

import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("population")
public record Population(
        UUID id,
        Integer rank,
        String countrySymbol,
        String countryName,
        String capital,
        String continent,
        Long population2022,
        Long population2020,
        Long population2015,
        Long population2010,
        Long population2000,
        Long population1990,
        Long population1980,
        Long population1970,
        Long area,
        Double density,
        Double growthRate,
        Double worldPopulationPercentage) {
}
