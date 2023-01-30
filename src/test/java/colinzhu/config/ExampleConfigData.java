package colinzhu.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ExampleConfigData {

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("csv-business-rules-engine");
        EntityManager em = emf.createEntityManager();
        ConfigRepository repo = new ConfigRepository(em);

        Config ruleConfig = new Config();
        ruleConfig.setName("high-value-check");
        ruleConfig.setContent("[{\"name\":\"UK-USD\",\"entity\":\"UK\",\"currency\":\"USD\",\"amount\":1000},{\"name\":\"UK-UKD\",\"entity\":\"UK\",\"currency\":\"HKD\",\"amount\":8000},{\"name\":\"UK-CNY\",\"entity\":\"UK\",\"currency\":\"CNY\",\"amount\":6000}]\n");
        ruleConfig.setIsActive(true);
        ruleConfig.setCreateTime(time);
        ruleConfig.setUpdateTime(time);

        Config ruleConfig2 = new Config();
        ruleConfig2.setName("high-value-pre-check");
        ruleConfig2.setContent("[{\"name\":\"UK\",\"entity\":\"UK\"},{\"name\":\"US\",\"entity\":\"US\"}]\n");
        ruleConfig2.setIsActive(true);
        ruleConfig2.setCreateTime(time);
        ruleConfig2.setUpdateTime(time);


        Config ruleConfig3 = new Config();
        ruleConfig3.setName("json-object");
        ruleConfig3.setContent("{\"name\":\"UK\",\"entity\":\"UK\"}");
        ruleConfig3.setIsActive(true);
        ruleConfig3.setCreateTime(time);
        ruleConfig3.setUpdateTime(time);

        Optional<Config> config = repo.save(ruleConfig);
        log.info("high-value-check:" + config);

        Optional<Config> config2 = repo.save(ruleConfig2);
        log.info("high-value-pre-check:" + config2);

        Optional<Config> config3 = repo.save(ruleConfig3);
        log.info("json-object:" + config3);
    }
}
