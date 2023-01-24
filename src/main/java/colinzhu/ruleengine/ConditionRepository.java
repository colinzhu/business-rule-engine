package colinzhu.ruleengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;

public class ConditionRepository {

    @SneakyThrows
    public static <T> T getCondition(ConditionType conditionType, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        String filePath = "condition/" + conditionType.type + ".json";
        String json = Files.readString(Path.of(ClassLoader.getSystemResource(filePath).toURI()));
        System.out.println(conditionType + ":\n" + json);
        return objectMapper.readValue(json, clazz);
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
