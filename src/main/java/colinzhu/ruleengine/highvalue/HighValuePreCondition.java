package colinzhu.ruleengine.highvalue;

import lombok.Data;

import java.util.function.Predicate;

@Data
public class HighValuePreCondition implements Predicate<HighValueCheckFact>{

  private String name;
  private String entity;

  public HighValuePreCondition() {
  }

  @Override
  public boolean test(HighValueCheckFact request) {
      boolean result = entity.equals(request.getEntity());
      if (result) {
        System.out.println("Y: " + this);
      } else {
        System.out.println("N: " + this);
      }
      return result;
  }
}
