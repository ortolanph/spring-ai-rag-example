package pt.pauloortolan.springairagexample.controllers;

import com.datastax.oss.driver.api.core.CqlSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cql")
@RequiredArgsConstructor
public class CqlBrowserController {

    private final CqlSession session;

    @GetMapping("/keyspaces")
    public List<String> keyspaces() {
        return session.execute("DESCRIBE keyspaces")
                .all().stream()
                .map(row -> row.getString(0))
                .toList();
    }

    @GetMapping("/tables/{keyspace}")
    public List<String> tables(@PathVariable String keyspace) {
        return session.execute(
                        "SELECT table_name FROM system_schema.tables WHERE keyspace_name=?", keyspace)
                .all().stream()
                .map(row -> row.getString("table_name"))
                .toList();
    }

    @PostMapping("/query")
    public List<Map<String, Object>> query(@RequestBody String cql) {
        return session.execute(cql).all().stream()
                .map(row -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    row.getColumnDefinitions()
                            .forEach(col -> map.put(col.getName().toString(),
                                    row.getObject(col.getName())));
                    return map;
                })
                .toList();
    }
}