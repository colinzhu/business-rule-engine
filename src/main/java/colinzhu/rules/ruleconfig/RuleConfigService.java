package colinzhu.rules.ruleconfig;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@RequiredArgsConstructor
public class RuleConfigService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RuleConfigRepository repo;

    @SneakyThrows
    public <T> T getConfigFromJson(String name) {
        return objectMapper.readValue(getContent(name), new TypeReference<>() {
        });
    }

    @SneakyThrows
    private String getContent(String name) {
        //if (name.endsWith(".json")) {
        URL classpathFileUrl = ClassLoader.getSystemResource("rule-config/" + name);
        if (classpathFileUrl != null) {
            log.info("Get Content from classpath: rule-config/" + name);
            Path filePath = Path.of(classpathFileUrl.toURI());
            String json = Files.readString(filePath);
            log.info(name + ":\n" + json);
            return json;
        }
        //}
        return repo.findByName(name).orElseThrow().getContent();
    }
}
