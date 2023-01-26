package colinzhu.ruleengine.payment.highvalue;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

@Data
@Slf4j
public class HighValuePreCondition implements Predicate<HighValueCheckFact>{
  private String name;
  private String entity;

  @Override
  public boolean test(HighValueCheckFact fact) {
    boolean result = entity.equals(fact.getEntity());
    log.info(result + " tested condition: " + this);
    return result;

  }
}
