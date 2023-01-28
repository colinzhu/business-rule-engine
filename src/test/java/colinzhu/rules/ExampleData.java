package colinzhu.rules;

import colinzhu.rules.ruleconfig.RuleConfig;
import colinzhu.rules.ruleconfig.RuleConfigRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ExampleData {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("csv-business-rules-engine");
        EntityManager em = emf.createEntityManager();
        RuleConfigRepository repo = new RuleConfigRepository(em);

        RuleConfig ruleConfig = new RuleConfig();
        ruleConfig.setName("high-value-check");
        ruleConfig.setContent("[{\"name\":\"UK-USD\",\"entity\":\"UK\",\"currency\":\"USD\",\"amount\":1000},{\"name\":\"UK-UKD\",\"entity\":\"UK\",\"currency\":\"HKD\",\"amount\":8000},{\"name\":\"UK-CNY\",\"entity\":\"UK\",\"currency\":\"CNY\",\"amount\":6000}]\n");
        ruleConfig.setIsActive(true);
        ruleConfig.setCreateTime(System.currentTimeMillis());

        RuleConfig ruleConfig2 = new RuleConfig();
        ruleConfig2.setName("high-value-pre-check");
        ruleConfig2.setContent("[{\"name\":\"UK\",\"entity\":\"UK\"},{\"name\":\"US\",\"entity\":\"US\"}]\n");
        ruleConfig2.setIsActive(true);
        ruleConfig2.setCreateTime(System.currentTimeMillis());


        RuleConfig ruleConfig3 = new RuleConfig();
        ruleConfig3.setName("json-object");
        ruleConfig3.setContent("{\"name\":\"UK\",\"entity\":\"UK\"}");
        ruleConfig3.setIsActive(true);
        ruleConfig3.setCreateTime(System.currentTimeMillis());

        Optional<RuleConfig> config = repo.save(ruleConfig);
        log.info("high-value-check:" + config);

        Optional<RuleConfig> config2 = repo.save(ruleConfig2);
        log.info("high-value-pre-check:" + config2);

        Optional<RuleConfig> config3 = repo.save(ruleConfig3);
        log.info("json-object:" + config3);
    }
}
