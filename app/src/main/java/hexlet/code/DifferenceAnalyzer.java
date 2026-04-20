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

            boolean inContent1 = content1.containsKey(key);
            boolean inContent2 = content2.containsKey(key);

            if (inContent1 && !inContent2) {
                changes.put("removed", content1.get(key));
            } else if (!inContent1 && inContent2) {
                changes.put("added", content2.get(key));
            } else {
                var value1 = content1.get(key);
                var value2 = content2.get(key);

                if ((((value1 != null) && (value1.equals(value2)))
                        || ((value1 == null) && (value2 == null)))) {
                    changes.put("same", value1);
                } else {
                    changes.put("removed", value1);
                    changes.put("added", value2);
                }
            }

            result.put(key, changes);
        }

        return result;
    }
}
