package colinzhu.rules;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Function;

public interface Rule extends Function<JsonNode, Result> {
}
