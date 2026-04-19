package hexlet.code.formatters;

import java.util.SortedMap;


public final class StylishFormatter implements hexlet.code.Formatter {


    public StylishFormatter() {
        // to use as a Formatter interface needs to create an object
    }

    @Override
    public String format(SortedMap<String, SortedMap<String, Object>> differences) {
        var sortedDifferencesList = differences.entrySet()
                .stream()
                .flatMap(e -> e.getValue()
                        .entrySet()
                        .stream()
                        .map(el -> {
                            var key = el.getKey();
                            var value = el.getValue();

                            if (key.equals("=")) {
                                key = " ";
                            }

                            return key + " " + e.getKey() + ": " + value;

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
