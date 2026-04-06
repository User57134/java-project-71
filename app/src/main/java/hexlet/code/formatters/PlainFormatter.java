package hexlet.code.formatters;

import java.util.HashMap;
import java.util.Map;

public final class PlainFormatter implements hexlet.code.Formatter {

    public PlainFormatter() {
        // to use as a Formatter interface needs to create an object
    }

    private static String makePlainValue(Object value) {
        if (value == null) {
            return "null";
        }

        if (value instanceof Boolean bValue) {
            return bValue.toString();
        }

        if (value instanceof Number nValue) {
            return nValue.toString();
        }

        String res = value.toString();
        if ((res.charAt(0) != '[') && (res.charAt(0) != '{')) {
            return "'" + value + "'";
        } else {
            return "[complex value]";
        }
    }

    @Override
    public String format(Map<String, HashMap<String, Object>> differences) {
        var sortedDifferencesList = differences.entrySet()
                .stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .map(el -> {
                    String message = "Property '";

                    var key = el.getKey();
                    var changes = el.getValue();
                    if (changes.size() == 2) {
                        String previousValue = makePlainValue(changes.get("-"));
                        String actualValue = makePlainValue(changes.get("+"));
                        message += key + "' was updated. From " + previousValue + " to " + actualValue;
                    } else if (changes.size() == 1) {
                        var changesEntry = changes.entrySet().stream().findAny().get();

                        switch (changesEntry.getKey()) {
                            case "+":
                                message += key + "' was added with value: " + makePlainValue(changesEntry.getValue());
                                break;

                            case "-":
                                message += key + "' was removed";
                                break;

                            default:
                                message = "";
                        }
                    }

                    return message;
                }).filter(s -> !s.isEmpty())
                .toList();

        StringBuilder builder = new StringBuilder();
        for (var line : sortedDifferencesList) {
            builder.append(line);
            builder.append("\n");
        }

        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
