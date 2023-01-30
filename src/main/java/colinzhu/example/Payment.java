package colinzhu.example;

import lombok.Data;

@Data
public class Payment {
    private String entity;
    private String currency;
    private Integer amount;
}
