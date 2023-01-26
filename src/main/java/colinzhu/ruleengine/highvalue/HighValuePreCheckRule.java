package colinzhu.ruleengine.highvalue;

import colinzhu.ruleengine.ConditionRepository;
import colinzhu.ruleengine.Result;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.function.Function;

public class HighValuePreCheckRule implements Function<HighValueCheckFact, Result> {
  private final HighValuePreCondition[] highValuePreConditions;
  private final HighValueCheckRule highValueCheckRule = new HighValueCheckRule();

  @SneakyThrows
  public HighValuePreCheckRule() {
    highValuePreConditions = ConditionRepository.getCondition(ConditionRepository.ConditionType.HIGH_VALUE_PRE_CONDITION, HighValuePreCondition[].class);
  }

  private boolean when(HighValueCheckFact fact) {
    return Arrays.stream(highValuePreConditions).anyMatch(condition -> condition.test(fact));
  }

  private Result otherwise(HighValueCheckFact fact) {
    return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.NA, "Not applicable.");
  }

  private Result then(HighValueCheckFact fact) {
    return highValueCheckRule.apply(fact);
  }

  @Override
  public Result apply(HighValueCheckFact fact) {
    if (when(fact)) {
      return then(fact);
    } else {
      return otherwise(fact);
    }
//    if (Arrays.stream(highValuePreConditions).anyMatch(condition -> condition.test(fact))) {
//      Optional<HighValueCondition> matchedCondition = Arrays.stream(highValueConditions).filter(condition -> condition.test(fact)).findFirst();
//      if (matchedCondition.isPresent()) {
//        return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.POSITIVE, "Condition: " + matchedCondition.get());
//      } else {
//        return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.NEGATIVE, "No condition matched.");
//      }
//    } else {
//      return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.NA, "Not applicable.");
//    }
  }

}
