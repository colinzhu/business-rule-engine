package colinzhu.rules.payment;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PaymentRuleEngine {
    private final List<Function<Payment,Result>> ruleList;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public PaymentRuleEngine(HighValueRule highValueRule) {
        ruleList = new ArrayList<>();
        ruleList.add(payment -> highValueRule.apply(objectMapper.valueToTree(payment)));
        // can add other business rules
    }

    public Result[] apply(Payment payment) {
        // invoke all the engines, combine all the results into an array
        return ruleList.parallelStream().map(engine -> engine.apply(payment)).toArray(Result[]::new);
    }

}
