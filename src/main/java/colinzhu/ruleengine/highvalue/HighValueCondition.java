package colinzhu.ruleengine.highvalue;

import lombok.Data;

import java.util.function.Predicate;

@Data
public class HighValueCondition implements Predicate<HighValueCheckFact>{
  private String name;
  private String entity;
  private String currency;
  private Integer amount;

  @Override
  public boolean test(HighValueCheckFact fact) {
    boolean result = entity.equals(fact.getEntity()) && currency.equals(fact.getCurrency()) && fact.getAmount() >= amount;
    System.out.println(result + " tested condition: " + this);
    return result;
  }
}
