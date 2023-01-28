package colinzhu.rules.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    String rule;
    Boolean matched;
    Object value;
    String remark;
}
