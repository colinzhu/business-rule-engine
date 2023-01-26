package colinzhu.ruleengine.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    ResultType type;
    ResultCode code;
    String remark;

    public enum ResultCode {
        POSITIVE, NEGATIVE, NA
    }
    public enum ResultType {
        HIGH_VALUE
    }
}
