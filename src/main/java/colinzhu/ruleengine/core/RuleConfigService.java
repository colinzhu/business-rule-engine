package colinzhu.ruleengine.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Slf4j
public class RuleConfigService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private RuleConfigService() {}

    @SneakyThrows
    public static List<Map> getConfig(String name) {
        return objectMapper.readValue(getConfigJson(name), objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
    }

    @SneakyThrows
    private static String getConfigJson(String name) {
        String filePath = "config/" + name + ".json";
        String json = Files.readString(Path.of(ClassLoader.getSystemResource(filePath).toURI()));
        log.info(name + ":\n" + json);
        return json;
    }
}
