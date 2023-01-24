package colinzhu.ruleengine.highvalue;

import lombok.Data;

@Data
public class HighValueCheckFact {
  private String entity;
  private String currency;
  private Integer amount;
}
