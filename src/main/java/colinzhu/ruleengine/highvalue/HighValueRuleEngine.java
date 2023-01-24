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
    if (Arrays.stream(highValuePreConditions).anyMatch(condition -> condition.test(fact))) {
      Optional<HighValueCondition> matchedCondition = Arrays.stream(highValueConditions).filter(condition -> condition.test(fact)).findFirst();
      if (matchedCondition.isPresent()) {
        System.out.println("Y." + matchedCondition.get());
        return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.POSITIVE, "Condition: " + matchedCondition.get());
      } else {
        return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.NEGATIVE, "No condition matched.");
      }
    } else {
      return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.NA, "Not applicable.");
    }
  }
}
