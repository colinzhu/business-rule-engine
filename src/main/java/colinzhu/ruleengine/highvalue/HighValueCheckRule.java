package colinzhu.ruleengine.highvalue;

import colinzhu.ruleengine.ConditionRepository;
import colinzhu.ruleengine.Result;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class HighValueCheckRule implements Function<HighValueCheckFact, Result> {
  private final HighValueCondition[] highValueConditions;

  @SneakyThrows
  public HighValueCheckRule() {
    highValueConditions = ConditionRepository.getCondition(ConditionRepository.ConditionType.HIGH_VALUE_CONDITION, HighValueCondition[].class);
  }
  private boolean when(Map context, HighValueCheckFact fact) {
    Optional<HighValueCondition> highValueCondition = Arrays.stream(highValueConditions).filter(condition -> condition.test(fact)).findFirst();
    if (highValueCondition.isPresent()) {
      context.put("MATCHED_CONDITION", highValueCondition.get());
      return true;
    }
    return false;
  }
  private Result then(Map context, HighValueCheckFact fact) {
    return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.POSITIVE, "Condition: " + context.get("MATCHED_CONDITION"));
  }
  private Result otherwise(Map context, HighValueCheckFact fact) {
    return new Result(Result.ResultType.HIGH_VALUE, Result.ResultCode.NEGATIVE, "No condition matched.");
  }

  @Override
  public Result apply(HighValueCheckFact fact) {
    Map context = new HashMap();
    if (when(context, fact)) {
      return then(context, fact);
    }
    return otherwise(context, fact);
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
