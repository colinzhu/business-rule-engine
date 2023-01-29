package colinzhu.rules;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import colinzhu.rules.engine.DefaultRule;
import colinzhu.rules.engine.Result;
import colinzhu.rules.engine.Rule;
import colinzhu.rules.engine.RulesEngine;
import colinzhu.rules.example.HighValueRule;
import colinzhu.rules.example.Payment;
import colinzhu.rules.example.PaymentCheckResultCode;
import colinzhu.rules.ruleconfig.RuleConfigRepository;
import colinzhu.rules.ruleconfig.RuleConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Slf4j
public class Example {
    public static void main(String[] args) {
        // init
        ObjectMapper objectMapper = new ObjectMapper();

        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("csv-business-rules-engine");
        EntityManager em = emf.createEntityManager();
        RuleConfigRepository repo = new RuleConfigRepository(em);
        RuleConfigService ruleConfigService = new RuleConfigService(repo);


        // Approach 1: custom rule class
        Rule highValueRule = new HighValueRule(ruleConfigService);
        RulesEngine engine = new RulesEngine(List.of(highValueRule));

        Payment payment = new Payment();
        payment.setEntity("UK");
        payment.setCurrency("HKD");
        payment.setAmount(80000);

        List<Result> results = engine.apply(objectMapper.valueToTree(payment));
        log.info(results.toString());

        // Approach 2: Use DefaultRule, chain 2 rules into 1
        Rule highValueCheckRule = DefaultRule.builder()
                .when(fact -> ruleConfigService.<List<Map>>parseConfigFromJson("example-high-value-check.json").stream().anyMatch(item -> item.get("entity").equals(fact.get("entity").textValue()) && item.get("currency").equals(fact.get("currency").textValue()) && ((Integer) item.get("amount")) <= fact.get("amount").asInt()))
                .then(fact -> new Result("HIGH_VALUE_CHECK", true, PaymentCheckResultCode.POSITIVE, "Rule matched."))
                .otherwise(fact -> new Result("HIGH_VALUE_CHECK", false, PaymentCheckResultCode.NEGATIVE, "No rule matched."))
                .build();

        Rule highValuePreCheckRule = DefaultRule.builder()
                .when(fact -> ruleConfigService.<List<Map>>parseConfigFromJson("high-value-pre-check").stream().anyMatch(item -> item.get("entity").equals(fact.get("entity").textValue())))
                .then(fact -> {
                    log.info("HIGH_VALUE_PRE_CHECK passed.");
                    return highValueCheckRule.apply(fact);
                })
                .otherwise(fact -> new Result("HIGH_VALUE_PRE_CHECK", false, PaymentCheckResultCode.NA, "Pre-check failed."))
                .build();

        RulesEngine engine2 = new RulesEngine(List.of(highValuePreCheckRule));
        List<Result> results2 = engine2.apply(objectMapper.valueToTree(payment));
        log.info(results2.toString());


        // Approach 3: Use DefaultRule, chain 2 rules with Policy.SEQ_STOP_IF_FAIL
        Rule highValuePreCheckRule3 = DefaultRule.builder()
                .when(fact -> ruleConfigService.<List<Map>>parseConfigFromJson("high-value-pre-check").stream().anyMatch(item -> item.get("entity").equals(fact.get("entity").textValue())))
                .then(fact -> new Result("HIGH_VALUE_PRE_CHECK", true, null, "Pre-check passed."))
                .otherwise(fact -> new Result("HIGH_VALUE_PRE_CHECK", false, PaymentCheckResultCode.NA, "Pre-check failed."))
                .build();

        RulesEngine engine3 = new RulesEngine(List.of(highValuePreCheckRule3, highValueCheckRule));
        engine3.setPolicy(RulesEngine.Policy.SEQ_STOP_IF_FAIL);
        List<Result> results3 = engine3.apply(objectMapper.valueToTree(payment));
        log.info(results3.toString());

    }
}
