package colinzhu.ruleengine.highvalue;

import lombok.Data;

import java.util.function.Predicate;

@Data
public class HighValueCondition {
  private String name;
  private String entity;
  private String currency;
  private Integer amount;
}
