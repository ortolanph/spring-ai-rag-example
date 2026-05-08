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
public class OscarNominee {

    private UUID id;
    private Integer yearCeremony;
    private String yearFile;
    private Integer ceremony;
    private String category;
    private String categoryName;
    private String name;
    private String film;
    private boolean winner;

}
