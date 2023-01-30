package colinzhu.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Slf4j
public class ConfigServiceTest {

    private final ConfigService configService;

    public ConfigServiceTest() {
        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("csv-business-rules-engine");
        EntityManager em = emf.createEntityManager();
        ConfigRepository repo = new ConfigRepository(em);
        configService = new ConfigService(repo);
        log.info("RuleConfigServiceTest initialized");
    }

    @Test
    public void getConfigFromJsonFromRepository() {
        Map configIsJsonObject = configService.parseConfigFromJson("json-object");
        log.info("configIsJsonObject: " + configIsJsonObject);

        List<Map> configIsJsonArray = configService.parseConfigFromJson("high-value-check");
        log.info("configIsJsonArray: " + configIsJsonArray);
    }

    @Test
    public void getConfigFromJsonFromClasspath() {
        List<Map> configIsJsonArray = configService.parseConfigFromJson("example-high-value-check.json");
        log.info("configIsJsonArray: " + configIsJsonArray);
    }
}