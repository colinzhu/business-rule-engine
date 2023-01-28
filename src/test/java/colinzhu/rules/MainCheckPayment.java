package colinzhu.rules;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import colinzhu.rules.core.DefaultRule;
import colinzhu.rules.core.Result;
import colinzhu.rules.core.Rule;
import colinzhu.rules.core.RuleEngine;
import colinzhu.rules.payment.HighValueRule;
import colinzhu.rules.payment.Payment;
import colinzhu.rules.payment.PaymentCheckResultCode;
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
public class MainCheckPayment {
    public static void main(String[] args) {
        // init
        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("csv-business-rules-engine");
        EntityManager em = emf.createEntityManager();
        RuleConfigRepository repo = new RuleConfigRepository(em);
        RuleConfigService ruleConfigService = new RuleConfigService(repo);

        List<Map> highValueCheckRuleConfig = ruleConfigService.getConfigAsListOfMap("high-value-check");
        List<Map> highValuePreCheckRuleConfig = ruleConfigService.getConfigAsListOfMap("high-value-pre-check");

        // Approach 1: custom rule class
        Rule highValueRule = new HighValueRule(highValueCheckRuleConfig, highValuePreCheckRuleConfig);
        RuleEngine engine = new RuleEngine(List.of(highValueRule));

        Payment payment = new Payment();
        payment.setEntity("UK");
        payment.setCurrency("HKD");
        payment.setAmount(80000);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Result> results = engine.apply(objectMapper.valueToTree(payment));
        log.info(results.toString());

        // Approach 2: Use DefaultRule, chain 2 rules into 1
        Rule highValueCheckRule = DefaultRule.builder()
                .when(fact -> highValueCheckRuleConfig.stream().anyMatch(item -> item.get("entity").equals(fact.get("entity").textValue()) && item.get("currency").equals(fact.get("currency").textValue()) && ((Integer) item.get("amount")) <= fact.get("amount").asInt()))
                .then(fact -> new Result("HIGH_VALUE_CHECK", true, PaymentCheckResultCode.POSITIVE, "Rule matched."))
                .otherwise(fact -> new Result("HIGH_VALUE_CHECK", false, PaymentCheckResultCode.NEGATIVE, "No rule matched."))
                .build();

        Rule highValuePreCheckRule = DefaultRule.builder()
                .when(fact -> highValuePreCheckRuleConfig.stream().anyMatch(item -> item.get("entity").equals(fact.get("entity").textValue())))
                .then(fact -> {
                    log.info("HIGH_VALUE_PRE_CHECK passed.");
                    return highValueCheckRule.apply(fact);
                })
                .otherwise(fact -> new Result("HIGH_VALUE_PRE_CHECK", false, PaymentCheckResultCode.NA, "Pre-check failed."))
                .build();

        RuleEngine engine2 = new RuleEngine(List.of(highValuePreCheckRule));
        List<Result> results2 = engine2.apply(objectMapper.valueToTree(payment));
        log.info(results2.toString());


        // Approach 3: Use DefaultRule, chain 2 rules with Policy.SEQ_STOP_IF_FAIL
        Rule highValuePreCheckRule3 = DefaultRule.builder()
                .when(fact -> highValuePreCheckRuleConfig.stream().anyMatch(item -> item.get("entity").equals(fact.get("entity").textValue())))
                .then(fact -> new Result("HIGH_VALUE_PRE_CHECK", true, null, "Pre-check passed."))
                .otherwise(fact -> new Result("HIGH_VALUE_PRE_CHECK", false, PaymentCheckResultCode.NA, "Pre-check failed."))
                .build();

        RuleEngine engine3 = new RuleEngine(List.of(highValuePreCheckRule3, highValueCheckRule));
        engine3.setPolicy(RuleEngine.Policy.SEQ_STOP_IF_FAIL);
        List<Result> results3 = engine3.apply(objectMapper.valueToTree(payment));
        log.info(results3.toString());

    }
}
