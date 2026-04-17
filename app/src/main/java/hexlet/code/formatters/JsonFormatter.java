package hexlet.code.formatters;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.SortedMap;
import java.util.TreeMap;


public final class JsonFormatter implements hexlet.code.Formatter {

    public JsonFormatter() {
        // to use as a Formatter interface needs to create an object
    }

    @Override
    public String format(SortedMap<String, TreeMap<String, Object>> differences) throws Exception {
        String result = null;

        ObjectMapper mapper = new ObjectMapper();

        result = mapper.writer().writeValueAsString(differences);

        return result;
    }
}
