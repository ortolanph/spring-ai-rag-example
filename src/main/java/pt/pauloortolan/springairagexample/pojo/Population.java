package pt.pauloortolan.springairagexample.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Population {

    private UUID id;
    private Integer rank;
    private String countrySymbol;
    private String countryName;
    private String capital;
    private String continent;
    private Long population2022;
    private Long population2020;
    private Long population2015;
    private Long population2010;
    private Long population2000;
    private Long population1990;
    private Long population1980;
    private Long population1970;
    private Long area;
    private Double density;
    private Double growthRate;
    private Double worldPopulationPercentage;

}
