package pt.pauloortolan.springairagexample.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RedisHash("climate")
public record Climate(
        @Id
        UUID id,
        String time,
        Double temperature,
        Double rain,
        Double cloud,
        Double humidity) {

    public Map<String, Object> toMetadata() {
        Map<String, Object> metadata = new HashMap<>();

        metadata.put("source", "Climate");
        metadata.put("class", getClass().getSimpleName());
        metadata.put("className", getClass().getName());
        metadata.put("id", id);
        metadata.put("time", time);
        metadata.put("temperature", temperature);
        metadata.put("rain", rain);
        metadata.put("cloud", cloud);
        metadata.put("humidity", humidity);

        return metadata;
    }

}