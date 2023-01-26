package colinzhu.ruleengine;

import colinzhu.ruleengine.payment.Payment;
import colinzhu.ruleengine.payment.PaymentRuleEngine;
import colinzhu.ruleengine.payment.Result;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class MainCheckPayment {
  public static void main(String[] args) {

    Payment payment = new Payment();
    payment.setEntity("UK");
    payment.setCurrency("HKD");
    payment.setAmount(80000);

    PaymentRuleEngine paymentRuleEngine = new PaymentRuleEngine();
    Result[] results = paymentRuleEngine.apply(payment);
    log.info(Arrays.toString(results));
  }



}
