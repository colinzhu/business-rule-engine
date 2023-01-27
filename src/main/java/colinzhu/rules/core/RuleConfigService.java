package colinzhu.rules.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Slf4j
public class RuleConfigService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RuleConfigRepository repo;

    public RuleConfigService(RuleConfigRepository repo) {
        this.repo = repo;
    }

    @SneakyThrows
    public List<Map> getConfig(String name) {
        return objectMapper.readValue(getJson(name), objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
    }

    @SneakyThrows
    private static String getConfigJson(String name) {
        String filePath = "config/" + name + ".json";
        String json = Files.readString(Path.of(ClassLoader.getSystemResource(filePath).toURI()));
        log.info(name + ":\n" + json);
        return json;
    }

    private String getJson(String name) {
        return repo.findByName(name).get().getContent();
    }

}
