package colinzhu.ruleengine.highvalue;

import lombok.Data;

import java.util.function.Predicate;

@Data
public class HighValuePreCondition implements Predicate<HighValueCheckFact>{
  private String name;
  private String entity;

  @Override
  public boolean test(HighValueCheckFact fact) {
    boolean result = entity.equals(fact.getEntity());
    System.out.println(result + " tested condition: " + this);
    return result;

  }
}
