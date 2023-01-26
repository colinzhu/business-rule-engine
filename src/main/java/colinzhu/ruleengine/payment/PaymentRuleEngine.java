package colinzhu.ruleengine.payment;

import colinzhu.ruleengine.Result;
import colinzhu.ruleengine.payment.Payment;
import colinzhu.ruleengine.payment.highvalue.HighValueCheckFact;
import colinzhu.ruleengine.payment.highvalue.HighValueRule;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PaymentRuleEngine {
    private final List<Function<Payment,Result>> engineList;

    public PaymentRuleEngine() {
        engineList = new ArrayList<>();
        engineList.add(payment -> new HighValueRule().apply(paymentToHighValueFact(payment)));
        // can add other business rule engines
    }

    public Result[] apply(Payment payment) {
        // invoke all the engines, combine all the results into an array
        return engineList.parallelStream().map(engine -> engine.apply(payment)).toArray(Result[]::new);
    }

    private static HighValueCheckFact paymentToHighValueFact(Payment payment) {
        // in order to de-couple Payment from HighValueRuleEngine
        HighValueCheckFact fact = new HighValueCheckFact();
        fact.setEntity(payment.getEntity());
        fact.setCurrency(payment.getCurrency());
        fact.setAmount(payment.getAmount());
        return fact;
    }

}
