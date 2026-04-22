package hexlet.code;

import com.fasterxml.jackson.core.JsonProcessingException;
import hexlet.code.formatters.JsonFormatter;
import hexlet.code.formatters.PlainFormatter;
import hexlet.code.formatters.StylishFormatter;

import java.util.LinkedHashMap;
import java.util.Map;


public interface Formatter {
    String format(Map<String, LinkedHashMap<String, Object>> differences) throws JsonProcessingException;
}


final class Formatters {

    private Formatters() {

    }

    public static Formatter getFormatter(String name) {
        if (name == null) {
            throw new RuntimeException("Formatter's name is not defined.");
        }

        switch (name) {
            case "stylish":
                return new StylishFormatter();
            case "plain":
                return new PlainFormatter();
            case "json":
                return new JsonFormatter();
            default:
                throw new RuntimeException("Unknown formatter name: " + name);
        }
    }
}
