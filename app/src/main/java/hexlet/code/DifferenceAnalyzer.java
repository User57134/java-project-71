package hexlet.code;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;


public final class DifferenceAnalyzer {
    private DifferenceAnalyzer() {

    }

    public static SortedMap<String, SortedMap<String, Object>> analyze(Map<String, Object> content1,
                                                                       Map<String, Object> content2) {
        // make a map with a Map<String, Object> as value:
        // - first is a line without changes
        // - second is a removed line
        var res1 = content1.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        el -> {
                            var result = new TreeMap<String, Object>(Collections.reverseOrder());
                            String key = el.getKey();
                            Object value = el.getValue();

                            if ((content2.containsKey(key))
                                    &&
                                    (((value != null) && (value.equals(content2.get(key))))
                                            || ((value == null) && (content2.get(key) == null)))) {
                                result.put("=", value);
                            } else {
                                result.put("-", value);
                            }
                            return result; },
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        }));

        // make a map with a Map<String, Object> as value:
        // - third is an added line
        var res2 = content2
                .entrySet()
                .stream()
                .filter(el -> {
                    String key = el.getKey();
                    Object value = el.getValue();

                    return ((!content1.containsKey(key))
                            ||
                            (((value != null) && (!value.equals(content1.get(key))))
                                    ||
                                    ((value == null) && (content1.get(key) != null)))); })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        el -> {
                            var result = new TreeMap<String, Object>(Collections.reverseOrder());
                            result.put("+", el.getValue());
                            return result; }));

        // make a map with a Map<String, Object> as value:
        // - first is a line without changes
        // - second is a removed line
        // - third is an added line
        var result = new TreeMap<String, SortedMap<String, Object>>(res1);
        res2.forEach((k, v) -> {
            if (result.containsKey(k)) {
                result.get(k).putAll(v);
            } else {
                result.put(k, v);
            }
        });

        return result;
    }
}
