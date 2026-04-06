package hexlet.code;

import hexlet.code.formatters.PlainFormatter;
import hexlet.code.formatters.StylishFormatter;

import java.util.HashMap;
import java.util.Map;

public interface Formatter {
    String format(Map<String, HashMap<String, Object>> differences);
}


final class Formatters {

    private Formatters() {

    }


    public static Formatter getFormatter(String name) throws Exception {
        switch (name) {
            case "stylish":
                return new StylishFormatter();
            case "plain":
                return new PlainFormatter();
            default:
                throw new Exception("There is no formatter: " + name);
        }
    }
}
