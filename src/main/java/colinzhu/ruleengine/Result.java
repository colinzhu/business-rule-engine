package colinzhu.ruleengine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class Result {
    String type;
    String code;
    String remark;

    public static class ResultCode {
        public static final String POSITIVE = "POSITIVE";
        public static final String NEGATIVE = "NEGATIVE";
        public static final String NA = "NA";
    }
    public static class ResultType {
        public static final String HIGH_VALUE = "HIGHVALUE";
    }
}
