package colinzhu.ruleengine.payment;

import colinzhu.ruleengine.core.RuleConfigService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HighValueRule {
  private final List<Map> highValueCheckRuleConfig = RuleConfigService.getConfig("high-value-check-rule-config");
  private final List<Map> highValuePreCheckRuleConfig = RuleConfigService.getConfig("high-value-pre-check-rule-config");

  public Result apply(JsonNode fact) {
    if (highValuePreCheckRuleConfig.stream().anyMatch(item -> item.get("entity").equals(fact.get("entity").textValue()))) {
      Optional<Map> matchConfigItem = highValueCheckRuleConfig.stream()
              .filter(item -> item.get("entity").equals(fact.get("entity").textValue()) && item.get("currency").equals(fact.get("currency").textValue()) && ((Integer)item.get("amount")) <= fact.get("amount").asInt())
              .findFirst();
      if (matchConfigItem.isPresent()) {
        return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.POSITIVE, "Rule matched: " + matchConfigItem.get());
      } else {
        return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.NEGATIVE, "No rule matched.");
      }
    } else {
      return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.NA, "Not applicable.");
    }
  }

}
