package colinzhu.rules.ruleconfig;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RuleConfigService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RuleConfigRepository repo;
    private final Map<String, RuleConfig> cache = new ConcurrentHashMap<>();

    public RuleConfigService(RuleConfigRepository repo) {
        this.repo = repo;
        TimerTask refreshCacheTask = new TimerTask() {
            @Override
            public void run() {
                // select all records (name, updateTime) from DB
                cache.forEach((k, v) -> log.info(k + "-> updateTime:" + v.getUpdateTime() + " DB latest updateTime: xxxx"));
                // if DB updateTime > cachedRuleConfig.updateTime, get RuleConfig from DB, update cache
                log.info("refreshCacheTask completed.");
            }
        };
        Timer timer = new Timer();
        timer.schedule(refreshCacheTask, 0, 10000);
    }

    @SneakyThrows
    public <T> T getConfigFromJson(String name) {
        return objectMapper.readValue(getContent(name).getContent(), new TypeReference<>(){});
    }

    @SneakyThrows
    private RuleConfig getContent(String name) {
        RuleConfig cachedRuleConfig = cache.get(name);
        if (null != cachedRuleConfig) {
            return cachedRuleConfig;
        }

        RuleConfig ruleConfig;
        URL classpathFileUrl = ClassLoader.getSystemResource("rule-config/" + name);
        if (classpathFileUrl != null) {
            log.info("Get Content from classpath: rule-config/" + name);
            Path filePath = Path.of(classpathFileUrl.toURI());
            String json = Files.readString(filePath);
            log.info(name + ":\n" + json);
            ruleConfig = new RuleConfig();
            ruleConfig.setName(name);
            ruleConfig.setUpdateTime(null);// TODO
        } else {
            ruleConfig = repo.findByName(name).orElseThrow();
        }
        cache.put(name, ruleConfig);
        return ruleConfig;
    }

    @Data
    @AllArgsConstructor
    private static class ObjectAndUpdateTime<T> {
        private T object;
        private Long updateTime;
    }
}
