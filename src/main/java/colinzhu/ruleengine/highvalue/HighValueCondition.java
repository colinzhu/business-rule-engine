package colinzhu.ruleengine.highvalue;

import lombok.Data;

import java.util.function.Predicate;

@Data
public class HighValueCondition implements Predicate<HighValueCheckFact>{

  private String name;
  private String entity;
  private String currency;
  private Integer amount;

  public HighValueCondition() {
  }

  @Override
  public boolean test(HighValueCheckFact request) {
      boolean result = entity.equals(request.getEntity()) && currency.equals(request.getCurrency()) && request.getAmount() >= amount;
      if (result) {
        System.out.println("Y: " + this);
      } else {
        System.out.println("N: " + this);
      }
      return result;
  }
}
