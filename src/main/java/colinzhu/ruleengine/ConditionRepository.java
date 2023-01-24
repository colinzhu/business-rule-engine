package colinzhu.ruleengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConditionRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final ConditionRepository instance = new ConditionRepository();

    private ConditionRepository() {}

    @SneakyThrows
    public static <T> T getCondition(ConditionType conditionType, Class<T> clazz) {
        return instance.objectMapper.readValue(getConditionJson(conditionType), clazz);
    }

    private static String getConditionJson(ConditionType conditionType) throws IOException, URISyntaxException {
        String filePath = "condition/" + conditionType.type + ".json";
        String json = Files.readString(Path.of(ClassLoader.getSystemResource(filePath).toURI()));
        System.out.println(conditionType + ":\n" + json);
        return json;
    }

    public enum ConditionType {
        HIGH_VALUE_CONDITION("high-value-condition"),
        HIGH_VALUE_PRE_CONDITION("high-value-pre-condition");
        private final String type;

        ConditionType(String type) {
            this.type = type;
        }
    }
}
