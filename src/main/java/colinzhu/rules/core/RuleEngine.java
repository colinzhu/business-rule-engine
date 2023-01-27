package colinzhu.rules.core;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RuleEngine {
    private final List<Rule> ruleList;

    public Result[] apply(JsonNode fact) {
        // invoke all the engines, combine all the results into an array
        return ruleList.parallelStream().map(rule -> rule.apply(fact)).toArray(Result[]::new);
    }
}
