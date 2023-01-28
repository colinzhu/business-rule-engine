package colinzhu.rules.core;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RuleEngine {
    private final List<Rule> ruleList;

    @Setter
    private Policy policy;

    public List<Result> apply(JsonNode fact) {
        // invoke all the engines, combine all the results into an array
        if (Policy.PARALLEL_ALL == policy) {
            // to be implemented in parallel mode
            return ruleList.parallelStream().map(rule -> rule.apply(fact)).collect(Collectors.toList());
        }
        if (Policy.SEQ_STOP_IF_FAIL == policy) {
            List<Result> results = new ArrayList<>();
            for (Rule rule : ruleList) {
                Result r = rule.apply(fact);
                results.add(r);
                if (!r.matched) {
                    break;
                }
            }
            return results;
        } else {
            return ruleList.parallelStream().map(rule -> rule.apply(fact)).collect(Collectors.toList());
        }
    }

    public enum Policy {
        PARALLEL_ALL, SEQ_STOP_IF_FAIL
    }
}
