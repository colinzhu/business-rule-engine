package colinzhu.rules.core;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RulesEngine {
    private final List<Rule> ruleList;

    @Setter
    private Policy policy;

    public List<Result> apply(JsonNode fact) {
        if (Policy.SEQ_ALL == policy) {
            return ruleList.stream().map(rule -> rule.apply(fact)).collect(Collectors.toList());
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
            // PARALLEL_ALL and default (null)
            // to be implemented in parallel mode
            return ruleList.parallelStream().map(rule -> rule.apply(fact)).collect(Collectors.toList());
        }
    }

    public enum Policy {
        PARALLEL_ALL, SEQ_ALL, SEQ_STOP_IF_FAIL
    }
}
