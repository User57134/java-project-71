package hexlet.code.formatters;

import java.util.HashMap;
import java.util.Map;


public final class StylishFormatter implements hexlet.code.Formatter {


    public StylishFormatter() {
        // to use as a Formatter interface needs to create an object
    }


    @Override
    public String format(Map<String, HashMap<String, Object>> differences) {
        var sortedDifferencesList = differences.entrySet()
                .stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .flatMap(e -> e.getValue()
                        .entrySet()
                        .stream()
                        .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                        .map(el -> el.getKey() + " " + e.getKey() + ": " + el.getValue()))
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
