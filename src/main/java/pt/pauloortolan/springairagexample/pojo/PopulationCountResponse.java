package pt.pauloortolan.springairagexample.pojo;

import java.util.List;

public record PopulationCountResponse(String category, List<PopulationCount> populationCounts) {
}
