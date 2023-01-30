package colinzhu.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ConfigService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConfigRepository repo;
    private final Map<String, ObjectWithUpdateTime> cache = new ConcurrentHashMap<>();

    public ConfigService(ConfigRepository repo) {
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
        timer.schedule(refreshCacheTask, 0, 5 * 60 * 1000);
    }

    @SneakyThrows
    public <T> T parseConfigFromJson(String name) {
        ObjectWithUpdateTime<T> cached = cache.get(name);
        if (null != cached) {
            log.info("Get parsed config from cache. Name: " + name);
            return cached.getObject();
        }
        log.info("Get parsed config from file / DB. Name: " + name);
        Config config = repo.findByName(name).orElseThrow();
        T t = objectMapper.readValue(config.getContent(), new TypeReference<>() {
        });
        cache.put(name, new ObjectWithUpdateTime<>(t, config.getUpdateTime()));

        return t;
    }

    @Data
    @AllArgsConstructor
    private static class ObjectWithUpdateTime<T> {
        private T object;
        private Long updateTime;
    }
}
