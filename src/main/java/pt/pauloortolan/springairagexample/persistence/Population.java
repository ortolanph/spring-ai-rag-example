package pt.pauloortolan.springairagexample.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RedisHash("population")
public record Population(
        @Id
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

    public Map<String, Object> toMetadata() {
        Map<String, Object> metadata = new HashMap<>();

        metadata.put("source", "Population");
        metadata.put("class", getClass().getSimpleName());
        metadata.put("className", getClass().getName());
        metadata.put("id", id);
        metadata.put("rank", rank);
        metadata.put("countrySymbol", countrySymbol);
        metadata.put("countryName", countryName);
        metadata.put("capital", capital);
        metadata.put("continent", continent);
        metadata.put("population2022", population2022);
        metadata.put("population2020", population2020);
        metadata.put("population2015", population2015);
        metadata.put("population2010", population2010);
        metadata.put("population2000", population2000);
        metadata.put("population1990", population1990);
        metadata.put("population1980", population1980);
        metadata.put("population1970", population1970);
        metadata.put("area", area);
        metadata.put("density", density);
        metadata.put("growthRate", growthRate);
        metadata.put("worldPopulationPercentage", worldPopulationPercentage);

        return metadata;
    }

}
