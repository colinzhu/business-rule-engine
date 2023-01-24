package colinzhu.ruleengine;

import colinzhu.ruleengine.highvalue.HighValueCondition;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;

public class ConditionRepository {

    @SneakyThrows
    public static <T> T getCondition(String type, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        String filePath = "condition/" + type + ".json";
        String json = Files.readString(Path.of(ClassLoader.getSystemResource(filePath).toURI()));
        System.out.println(type + ":\n" + json);
        return objectMapper.readValue(json, clazz);
    }

    public static class ConditionType {
        public static final String HIGH_VALUE_PRE_CONDITION = "high-value-pre-condition";
        public static final String HIGH_VALUE_CONDITION = "high-value-condition";
    }
}
