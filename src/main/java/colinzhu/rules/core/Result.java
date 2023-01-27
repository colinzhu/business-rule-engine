package colinzhu.rules.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    String rule;
    Object value;
    String remark;
}
