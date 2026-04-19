package hexlet.code;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedHashMap;


public final class DifferenceAnalyzer {

    private DifferenceAnalyzer() {

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
