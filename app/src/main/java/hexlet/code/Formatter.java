package hexlet.code;

import hexlet.code.formatters.JsonFormatter;
import hexlet.code.formatters.PlainFormatter;
import hexlet.code.formatters.StylishFormatter;

import java.util.TreeMap;

public interface Formatter {
    String format(TreeMap<String, TreeMap<String, Object>> differences) throws Exception;
}


final class Formatters {

    private Formatters() {

    }


    public static Formatter getFormatter(String name) throws Exception {
        if (name == null) {
            throw new Exception("A format is not specified: null");
        }

        switch (name) {
            case "stylish":
                return new StylishFormatter();
            case "plain":
                return new PlainFormatter();
            case "json":
                return new JsonFormatter();
            default:
                throw new Exception("There is no formatter for " + name);
        }
    }
}
