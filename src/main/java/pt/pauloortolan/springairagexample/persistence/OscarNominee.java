package pt.pauloortolan.springairagexample.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RedisHash("oscar_nominee")
public record OscarNominee(
        @Id
        UUID id,
        Integer yearCeremony,
        String yearFilm,
        Integer ceremony,
        String category,
        String categoryName,
        String name,
        String film,
        boolean winner) {

    public Map<String, Object> toMetadata() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("id", id);
        metadata.put("yearCeremony", yearCeremony);
        metadata.put("yearFilm", yearFilm);
        metadata.put("ceremony", ceremony);
        metadata.put("category", category);
        metadata.put("categoryName", categoryName);
        metadata.put("name", name);
        metadata.put("film", film);
        metadata.put("winner", winner);

        return metadata;
    }

}
