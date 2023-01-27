package colinzhu.rules;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import colinzhu.rules.core.*;
import colinzhu.rules.payment.HighValueRule;
import colinzhu.rules.payment.Payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
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

    List<Map> highValueCheckRuleConfig = ruleConfigService.getConfig("high-value-check");
    List<Map> highValuePreCheckRuleConfig = ruleConfigService.getConfig("high-value-pre-check");

    Rule highValueRule = new HighValueRule(highValueCheckRuleConfig, highValuePreCheckRuleConfig);
    RuleEngine engine = new RuleEngine(List.of(highValueRule));

    Payment payment = new Payment();
    payment.setEntity("UK");
    payment.setCurrency("HKD");
    payment.setAmount(80000);

    ObjectMapper objectMapper = new ObjectMapper();
    Result[] results = engine.apply(objectMapper.valueToTree(payment));
    log.info(Arrays.toString(results));
  }
}
