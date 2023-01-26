package colinzhu.ruleengine.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Slf4j
public class RuleConfigService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private RuleConfigService() {}

    @SneakyThrows
    public static List<Map> getCondition(Type type) {
        return objectMapper.readValue(getConditionJson(type), objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
    }

    private static String getConditionJson(Type type) throws IOException, URISyntaxException {
        String filePath = "config/" + type.type + ".json";
        String json = Files.readString(Path.of(ClassLoader.getSystemResource(filePath).toURI()));
        log.info(type + ":\n" + json);
        return json;
    }

    public enum Type {
        HIGH_VALUE_CHECK("high-value-check-rule-config"),
        HIGH_VALUE_PRE_CHECK("high-value-pre-check-rule-config");
        private final String type;

        Type(String type) {
            this.type = type;
        }
    }
}
