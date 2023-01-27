package colinzhu.rules.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    String rule;
    Enum code;
    String remark;
}
