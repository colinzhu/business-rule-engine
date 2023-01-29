package colinzhu.rules.engine;

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
