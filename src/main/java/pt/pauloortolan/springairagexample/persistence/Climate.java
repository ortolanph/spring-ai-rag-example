package pt.pauloortolan.springairagexample.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("climate")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Climate {

    private UUID id;
    private LocalDateTime time;
    private Double temperature;
    private Double rain;
    private Double cloud;
    private Double humidity;

}
