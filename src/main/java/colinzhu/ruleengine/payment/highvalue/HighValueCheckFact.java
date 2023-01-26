package colinzhu.ruleengine.payment.highvalue;

import lombok.Data;

@Data
public class HighValueCheckFact {
  private String entity;
  private String currency;
  private Integer amount;
}
