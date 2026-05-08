package pt.pauloortolan.springairagexample.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

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
