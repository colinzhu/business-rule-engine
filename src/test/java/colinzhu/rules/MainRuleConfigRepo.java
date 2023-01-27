package colinzhu.rules;

import colinzhu.rules.core.RuleConfig;
import colinzhu.rules.core.RuleConfigRepository;
import jakarta.persistence.*;

import java.util.Optional;

public class MainRuleConfigRepo {

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

        Optional<RuleConfig> config = repo.save(ruleConfig);
        System.out.println(config);

        Optional<RuleConfig> config2 = repo.save(ruleConfig2);
        System.out.println(config2);
    }
}
