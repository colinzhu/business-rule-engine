package colinzhu.ruleengine.highvalue;

import colinzhu.ruleengine.ConditionRepository;
import colinzhu.ruleengine.Result;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public class HighValueRuleEngine implements Function<HighValueCheckFact, Result> {
  private final HighValueCondition[] highValueConditions;
  private final HighValuePreCondition[] highValuePreConditions;

  @SneakyThrows
  public HighValueRuleEngine() {
    highValueConditions = ConditionRepository.getCondition(ConditionRepository.ConditionType.HIGH_VALUE_CONDITION, HighValueCondition[].class);
    highValuePreConditions = ConditionRepository.getCondition(ConditionRepository.ConditionType.HIGH_VALUE_PRE_CONDITION, HighValuePreCondition[].class);
  }

  @Override
  public Result apply(HighValueCheckFact fact) {
    if (Arrays.stream(highValuePreConditions).anyMatch(condition -> test(fact, condition))) {
      Optional<HighValueCondition> matchedCondition = Arrays.stream(highValueConditions).filter(condition -> test(fact, condition)).findFirst();
      if (matchedCondition.isPresent()) {
        return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.POSITIVE, "Condition: " + matchedCondition.get());
      } else {
        return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.NEGATIVE, "No condition matched.");
      }
    } else {
      return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.NA, "Not applicable.");
    }
  }

  private boolean test(HighValueCheckFact fact, HighValueCondition condition) {
    boolean result = condition.getEntity().equals(fact.getEntity()) && condition.getCurrency().equals(fact.getCurrency()) && fact.getAmount() >= condition.getAmount();
    System.out.println(result + " tested condition: " + condition);
    return result;
  }

  private boolean test(HighValueCheckFact fact, HighValuePreCondition condition) {
    boolean result = condition.getEntity().equals(fact.getEntity());
    System.out.println(result + " tested condition: " + condition);
    return result;
  }
}
