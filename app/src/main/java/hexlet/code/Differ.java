package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


enum InputFormat {
    JSON,
    YAML
}

final class Parser {
    private Parser() {

    }

    public static Map<String, Object> parse(String filename, InputFormat format) throws Exception {
        var p = Paths.get(filename);
        var content = Files.readString(p);

        if ((content == null) || (content.isEmpty())) {
            throw new Exception("Invalid argument: " + filename);
        }

        ObjectMapper mapper;
        switch (format) {
            case JSON:
                mapper = new ObjectMapper();
                break;
            case YAML:
                mapper = new YAMLMapper();
                break;
            default:
                throw new Exception("Wrong format of the input files");

        }

        return mapper.readValue(content, new TypeReference<Map<String, Object>>() { });
    }
}


@CommandLine.Command(name = "gendiff", mixinStandardHelpOptions = true, version = "checksum 4.0",
        description = "Compares two configuration files and shows a difference.")
class Differ implements Callable<Integer> {

    private static String getFixture(String filename) throws Exception {
        var path = Paths.get(filename).toAbsolutePath();
        return Files.readString(path).trim();
    }

    static String getFileExtension(String filename) {
        if ((filename == null) || (filename.isEmpty())) {
            return null;
        }

        int i = filename.lastIndexOf('.');
        if (i != -1) {
            return filename.substring(i + 1).toLowerCase();
        }

        return null;
    }


    private static String makeStylishDiff(Map<String, HashMap<String, Object>> differences) {
        var sortedDifferencesList = differences.entrySet()
                .stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .flatMap(e -> e.getValue()
                        .entrySet()
                        .stream()
                        .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                        .map(el -> {
                            return el.getKey() + " " + e.getKey() + ": " + el.getValue();
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


    private static String makePlainDiff(Map<String, HashMap<String, Object>> differences) {
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
    }


    private static InputFormat defineInputFormat(String filename1, String filename2) {
        InputFormat format = null;

        String extension = getFileExtension(filename1);
        if ((extension != null) && (extension.equals(getFileExtension(filename2)))) {
            if (extension.equals("json")) {
                format = InputFormat.JSON;
            } else if (extension.equals("yml")) {
                format = InputFormat.YAML;
            }
        }

        return format;
    }


    private static String viewDiffAs(Map<String, HashMap<String, Object>> differences, String format) {
        if (format.equals("stylish")) {
            return makeStylishDiff(differences);
        } else {
            return makePlainDiff(differences);
        }
    }


    public static String generate(String filename1, String filename2, String viewFormat) throws Exception {
        InputFormat type = defineInputFormat(filename1, filename2);

        if (type == null) {
            throw new Exception("Wrong input files");
        }

        var content1 = Parser.parse(filename1, type);
        var content2 = Parser.parse(filename2, type);

        // make a map with a List<String> as value:
        // - first is a line without changes
        // - second is a removed line
        var res1 = content1.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        el -> el.getKey(),
                        el -> {
                            var result = new HashMap<String, Object>();
                            String key = el.getKey();
                            Object value = el.getValue();

                            if ((content2.containsKey(key))
                                    &&
                                    (((value != null) && (value.equals(content2.get(key))))
                                            || ((value == null) && (content2.get(key) == null)))) {
                                result.put(" ", value);
                            } else {
                                result.put("-", value);
                            }
                            return result; },
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        }));

        // make a map with a List<String> as value:
        // - third is an added line
        var res2 = content2
                .entrySet()
                .stream()
                .filter(el -> {
                    String key = el.getKey();
                    Object value = el.getValue();

                    return ((!content1.containsKey(key))
                            ||
                            (((value != null) && (!value.equals(content1.get(key))))
                                    ||
                                    ((value == null) && (content1.get(key) != null)))); })
                .collect(Collectors.toMap(
                        el -> el.getKey(),
                        el -> {
                            var result = new HashMap<String, Object>();
                            result.put("+", el.getValue());
                            return result; }));

        // make a map with a List<String> as value:
        // - first is a line without changes
        // - second is a removed line
        // - third is an added line
        var result = new HashMap<String, HashMap<String, Object>>(res1);
        res2.forEach((k, v) -> {
            if (result.containsKey(k)) {
                result.get(k).putAll(v);
            } else {
                result.put(k, v);
            }
        });

        return viewDiffAs(result, viewFormat);
    }

    @CommandLine.Parameters(paramLabel = "filepath1", defaultValue = "", description = "path to first file")
    private String filepath1;

    @CommandLine.Parameters(paramLabel = "filepath2", defaultValue = "", description = "path to second file")
    private String filepath2;

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "Show this help message and exit")
    private boolean help = false;

    @CommandLine.Option(names = {"-V", "--version"}, description = "Print version information and exit")
    private boolean version = false;

    @CommandLine.Option(names = {"-f", "--format"}, defaultValue = "stylish", paramLabel = "FORMAT",
            description = "output format [default: stylish]")
    private String format;

    @Override
    public Integer call() throws Exception { // your business logic goes here...
        filepath1 = "src/test/resources/fixtures/file1.json";
        filepath2 = "src/test/resources/fixtures/file2.json";
        format = "plain";

        var result = generate(filepath1, filepath2, format);
        var expected = getFixture("src/test/resources/fixtures/result_plain.txt");

        if (result.equals(expected)) {
            System.out.println("got!");
        }

        if (result != null) {
            System.out.println(result);
        }

        return 0;
    }
}
