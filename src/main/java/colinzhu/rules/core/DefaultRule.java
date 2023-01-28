package colinzhu.rules.core;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

import java.util.function.Function;
import java.util.function.Predicate;

@Data
@Builder
public class DefaultRule implements Rule {
    private Predicate<JsonNode> when;
    private Function<JsonNode, Result> then;
    private Function<JsonNode, Result> otherwise;

    @Override
    public Result apply(JsonNode jsonNode) {
        if (when.test(jsonNode)) {
            return then.apply(jsonNode);
        }
        return otherwise.apply(jsonNode);
    }
}
