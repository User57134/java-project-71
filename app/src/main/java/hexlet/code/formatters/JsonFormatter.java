package hexlet.code.formatters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public final class JsonFormatter implements hexlet.code.Formatter {

    public JsonFormatter() {
        // to use as a Formatter interface needs to create an object
    }


    @Override
    public String format(Map<String, HashMap<String, Object>> differences) {
        String result = null;

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

            result = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(sortedDifferences);

        } catch (JsonProcessingException e) {

            // result is null
        }

        return result;
    }
}
