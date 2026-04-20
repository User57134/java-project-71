package hexlet.code.formatters;

import hexlet.code.Formatter;
import java.util.LinkedHashMap;
import java.util.Map;


public final class StylishFormatter implements Formatter {


    public StylishFormatter() {
        // to use as a Formatter interface needs to create an object
    }

    @Override
    public String format(Map<String, LinkedHashMap<String, Object>> differences) {
        var sortedDifferencesList = differences.entrySet()
                .stream()
                .flatMap(e -> e.getValue()
                        .entrySet()
                        .stream()
                        .map(el -> {
                            var sign = Formatter.getSign(el.getKey());
                            var value = el.getValue();

                            return sign + " " + e.getKey() + ": " + value;

                        }))
                .toList();

        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\n");

        for (var line : sortedDifferencesList) {
            builder.append("  ");
            builder.append(line);
            builder.append("\n");
        }

        builder.append("}");

        return builder.toString();
    }
}
