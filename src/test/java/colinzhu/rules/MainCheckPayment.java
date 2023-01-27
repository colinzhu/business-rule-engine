package colinzhu.rules;

import colinzhu.rules.core.RuleConfigRepository;
import colinzhu.rules.core.RuleConfigService;
import colinzhu.rules.payment.HighValueRule;
import colinzhu.rules.payment.Payment;
import colinzhu.rules.payment.PaymentRuleEngine;
import colinzhu.rules.payment.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

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

    HighValueRule highValueRule = new HighValueRule(highValueCheckRuleConfig, highValuePreCheckRuleConfig);
    PaymentRuleEngine paymentRuleEngine = new PaymentRuleEngine(highValueRule);

    Payment payment = new Payment();
    payment.setEntity("UK");
    payment.setCurrency("HKD");
    payment.setAmount(80000);

    Result[] results = paymentRuleEngine.apply(payment);
    log.info(Arrays.toString(results));
  }



}
