package pt.pauloortolan.springairagexample.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("oscar_nominee")
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
