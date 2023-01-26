package colinzhu.ruleengine.payment.highvalue;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

@Data
@Slf4j
public class HighValueCondition implements Predicate<HighValueCheckFact>{
  private String name;
  private String entity;
  private String currency;
  private Integer amount;

  @Override
  public boolean test(HighValueCheckFact fact) {
    boolean result = entity.equals(fact.getEntity()) && currency.equals(fact.getCurrency()) && fact.getAmount() >= amount;
    log.info(result + " tested condition: " + this);
    return result;
  }
}
