package colinzhu.rules.example;

import colinzhu.rules.engine.Result;
import colinzhu.rules.engine.Rule;
import colinzhu.rules.ruleconfig.RuleConfigService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class HighValueRule implements Rule {
    private static final String RULE_NAME = "HIGH_VALUE";
    private final RuleConfigService ruleConfigService;

    @Override
    public Result apply(JsonNode fact) {
        List<Map> highValueCheckRuleConfig = ruleConfigService.parseConfigFromJson("example-high-value-check.json");
        List<Map> highValuePreCheckRuleConfig = ruleConfigService.parseConfigFromJson("high-value-pre-check");

        if (highValuePreCheckRuleConfig.stream().anyMatch(item -> item.get("entity").equals(fact.get("entity").textValue()))) {
            Optional<Map> matchConfigItem = highValueCheckRuleConfig.stream()
                    .filter(item -> item.get("entity").equals(fact.get("entity").textValue()) && item.get("currency").equals(fact.get("currency").textValue()) && ((Integer) item.get("amount")) <= fact.get("amount").asInt())
                    .findFirst();
            if (matchConfigItem.isPresent()) {
                return new Result(RULE_NAME, true, PaymentCheckResultCode.POSITIVE, "Rule matched: " + matchConfigItem.get());
            } else {
                return new Result(RULE_NAME, false, PaymentCheckResultCode.NEGATIVE, "No rule matched.");
            }
        } else {
            return new Result(RULE_NAME, false, PaymentCheckResultCode.NA, "Pre-check no matched.");
        }
    }

}
