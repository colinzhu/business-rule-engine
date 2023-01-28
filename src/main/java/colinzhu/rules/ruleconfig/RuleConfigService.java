package colinzhu.rules.ruleconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class RuleConfigService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RuleConfigRepository repo;

    @SneakyThrows
    private static String getContentFromFile(String filename) {
        String filePath = "rule-config/" + filename;
        String json = Files.readString(Path.of(ClassLoader.getSystemResource(filePath).toURI()));
        log.info(filename + ":\n" + json);
        return json;
    }

    @SneakyThrows
    public List<Map> getConfigAsListOfMap(String name) {
        return objectMapper.readValue(getContent(name), objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
    }

    private String getContent(String name) {
        return repo.findByName(name).orElseThrow().getContent();
    }

}
