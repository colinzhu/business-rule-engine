package colinzhu;

import colinzhu.ruleengine.PaymentRuleEngine;
import colinzhu.ruleengine.Result;

import java.util.Arrays;

public class MainCheck {
  public static void main(String[] args) {

    Payment payment = new Payment();
    payment.setEntity("UK");
    payment.setCurrency("HKD");
    payment.setAmount(80000);

    PaymentRuleEngine paymentRuleEngine = new PaymentRuleEngine();
    Result[] results = paymentRuleEngine.apply(payment);
    System.out.println(Arrays.toString(results));
  }



}
