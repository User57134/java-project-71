package hexlet.code.formatters;

import java.util.HashMap;
import java.util.Map;

public final class PlainFormatter implements hexlet.code.Formatter {
    private static String makePlainValue(Object value) {
        if (value == null) {
            return "null";
        }

        if (value instanceof Boolean) {
            Boolean res = (Boolean) value;
            return res.toString();
        }

        if (value instanceof Number) {
            Number res = (Number) value;
            return res.toString();
        }

        String res = value.toString();
        if ((res.charAt(0) != '[') && (res.charAt(0) != '{')) {
            return "'" + value + "'";
        } else {
            return "[complex value]";
        }
    }

    @Override
    public String formate(Map<String, HashMap<String, Object>> differences) {
            var sortedDifferencesList = differences.entrySet()
                    .stream()
                    .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                    .map(el -> {
                        String message = null;

                        var key = el.getKey();
                        var changes = el.getValue();
                        if (changes.size() == 2) {
                            String previousValue = makePlainValue(changes.get("-"));
                            String actualValue = makePlainValue(changes.get("+"));
                            return "Property '" + key + "' was updated. From " + previousValue + " to " + actualValue;
                        } else if (changes.size() == 1) {
                            var changesEntry = changes.entrySet().stream().findAny().get();

                            switch (changesEntry.getKey()) {
                                case "+":
                                    message = "Property '" + key + "' was added with value: "
                                            + makePlainValue(changesEntry.getValue());
                                    break;

                                case "-":
                                    message = "Property '" + key + "' was removed";
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
        };
    }
}