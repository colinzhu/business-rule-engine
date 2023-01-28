package colinzhu.rules.ruleconfig;

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
public class RuleConfigServiceTest {

    private final RuleConfigService ruleConfigService;

    public RuleConfigServiceTest() {
        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("csv-business-rules-engine");
        EntityManager em = emf.createEntityManager();
        RuleConfigRepository repo = new RuleConfigRepository(em);
        ruleConfigService = new RuleConfigService(repo);
        log.info("RuleConfigServiceTest initialized");
    }

    @Test
    public void getConfigFromJsonFromRepository() {
        Map configIsJsonObject = ruleConfigService.getConfigFromJson("json-object");
        log.info("configIsJsonObject: " + configIsJsonObject);

        List<Map> configIsJsonArray = ruleConfigService.getConfigFromJson("high-value-check");
        log.info("configIsJsonArray: " + configIsJsonArray);
    }

    @Test
    public void getConfigFromJsonFromClasspath() {
        List<Map> configIsJsonArray = ruleConfigService.getConfigFromJson("high-value-check.json");
        log.info("configIsJsonArray: " + configIsJsonArray);
    }
}