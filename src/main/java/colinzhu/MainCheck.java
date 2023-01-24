package colinzhu;

import colinzhu.ruleengine.PaymentRuleEngine;
import colinzhu.ruleengine.Result;
import colinzhu.ruleengine.highvalue.HighValueCheckFact;
import colinzhu.ruleengine.highvalue.HighValueRuleEngine;

import java.util.Arrays;
import java.util.function.Function;

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
