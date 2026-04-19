package hexlet.code;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.Collections;


public final class DifferenceAnalyzer {

    private DifferenceAnalyzer() {

    }

    public static SortedMap<String, SortedMap<String, Object>> analyzeOld(Map<String, Object> content1,
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


    public static Map<String, LinkedHashMap<String, Object>> analyze(Map<String, Object> content1,
                                                                             Map<String, Object> content2) {
        //sort
        Set<String> set = new TreeSet<>(content1.keySet());
        Set<String> set2 = new TreeSet<>(content2.keySet());
        set.addAll(set2);

        var result = new LinkedHashMap<String, LinkedHashMap<String, Object>>();

        for (String key : set) {
            var changes = new LinkedHashMap<String, Object>();

            if (content1.containsKey(key)) {
                var value1 = content1.get(key);

                if (content2.containsKey(key)) {
                    var value2 = content2.get(key);

                    if (((value1 != null) && (value1.equals(value2)))
                            || ((value1 == null) && (value2 == null))) {
                        changes.put("=", value1);
                    } else {
                        changes.put("-", value1);
                        changes.put("+", value2);
                    }
                } else {
                    changes.put("-", value1);
                }

            } else if (content2.containsKey(key)) {
                changes.put("+", content2.get(key));
            }

            result.put(key, changes);
        }

        return result;
    }
}
