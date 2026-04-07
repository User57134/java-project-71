package hexlet.code.formatters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public final class JsonFormatter implements hexlet.code.Formatter {

    public JsonFormatter() {
        // to use as a Formatter interface needs to create an object
    }

//    private static String makeJsonValue(Object value, String gap) {
//        if (value == null) {
//            return "null";
//        }
//
//        if (value instanceof Map<?, ?> mp) {
//            StringBuilder builder = new StringBuilder();
//            builder.append("{\n");
//
//            for (var entry : mp.entrySet()) {
//                builder.append(gap + "    \"" + entry.getKey() + "\"" + " : "
//                        + makeJsonValue(entry.getValue(), "      "));
//                builder.append(",\n");
//            }
//
//            builder.delete(builder.length() - 2, builder.length());
//
//            builder.append("\n" + gap + "  }");
//            return builder.toString();
//        }
//
//        return value.toString();
//    }


    @Override
    public String format(Map<String, HashMap<String, Object>> differences) {
        String result = null;

//        StringBuilder builder = new StringBuilder();
//
//        var lines = differences.entrySet()
//                .stream()
//                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
//                .map(el -> {
//                    var key = el.getKey();
//                    int trap = 0;
//                    if (key.equals("obj1")) {
//                        trap = 1;
//                    }
//                    var changes = el.getValue();
//
//                    String res = "\"" + key + "\" : {\n";
//
//                    boolean isConst = false;
//                    if (changes.containsKey("=")) {
//                        var value = changes.get("=");
//                        var svalue = makeJsonValue(value, "    ");
//                        res += "      \"=\"" + ": " + svalue;
//                        isConst = true;
//                    }
//
//                    if (!isConst) {
//                        boolean previousValueWasProcessed = false;
//                        if (changes.containsKey("-")) {
//                            var value = changes.get("-");
//                            var svalue = makeJsonValue(value, "    ");
//                            res += "      \"-\"" + ": " + svalue;
//                            previousValueWasProcessed = true;
//                        }
//
//                        if (changes.containsKey("+")) {
//                            if (previousValueWasProcessed) {
//                                res += ", \n";
//                            }
//
//                            var value = changes.get("+");
//                            var svalue = makeJsonValue(value, "    ");
//                            res += "      \"+\"" + ": " + svalue;
//                        }
//                    }
//
//                    res += "\n    }";
//
//                    return res;
//
//                })
//                .toList();
//
//        builder.append("{\n");
//
//        for (int i = 0; i < lines.size(); i += 1) {
//            var line = lines.get(i);
//            if (!line.isEmpty()) {
//                builder.append("    ");
//                builder.append(line);
//
//                if (i != lines.size() - 1) {
//                    builder.append(",");
//                }
//                builder.append("\n");
//            }
//        }
//
//        builder.append("}");

       // return builder.toString();

        // Create a sorted by a key map and a submap for changes that sorted in reverse order
        // for printing removing operations before adding operations
        TreeMap<String, TreeMap<String, Object>> sortedDifferences = new TreeMap<>();
        for (var entry : differences.entrySet()) {
            var changes = new TreeMap<String, Object>(Collections.reverseOrder());
            changes.putAll(entry.getValue());

            sortedDifferences.put(entry.getKey(), changes);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();

            // Sort Map entries by keys
            //mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

            result = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(sortedDifferences);

        } catch (JsonProcessingException e) { }

        return result;
    }
}
