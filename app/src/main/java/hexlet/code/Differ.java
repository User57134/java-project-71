package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
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

    public static String getFileExtension(String filename) {
        if ((filename == null) || (filename.isEmpty())) {
            return null;
        }

        int i = filename.lastIndexOf('.');
        if (i != -1) {
            return filename.substring(i + 1).toLowerCase();
        }

        return null;
    }


    private static String makeStylishDiff(List<String> diff) {
        StringBuilder builder = new StringBuilder();

        builder.append("{");
        builder.append("\n");

        for (var line : diff) {
            builder.append("  ");
            builder.append(line);
            builder.append("\n");
        }

        builder.append("}");

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


    public static String generate(String filename1, String filename2, String inputFormat) throws Exception {
        InputFormat type = defineInputFormat(filename1, filename2);

        if (type == null) {
            throw new Exception("Wrong input files");
        }

        var content1 = Parser.parse(filename1, type);
        var content2 = Parser.parse(filename2, type);

        // lines, that were removed and lines with no changes
        var res1 = content1.entrySet()
                .stream()
                // make a map with a surrogate keys (_m) to sort it by value with the special rules:
                // - first is a line without changes
                // - second is a removed line
                // - third is an added line
                .collect(Collectors.toMap(el -> {
                    String key = el.getKey();
                    Object value = el.getValue();

                    if ((content2.containsKey(key))
                            &&
                            (((value != null) && (value.equals(content2.get(key))))
                                    ||
                                    ((value == null) && (content2.get(key) == null)))) {
                        return key;
                    } else {
                        return key + "_m";
                    }

                }, el -> el.getKey() + ": " + el.getValue()));

        // lines, that were added
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
                // make a map with a surrogate keys (_p) to sort it by value with the special rules:
                // - first is a line without changes
                // - second is a removed line
                // - third is an added line
                .collect(Collectors.toMap(el -> el.getKey() + "_p",
                        el -> el.getKey() + ": " + el.getValue()));

        var result = new HashMap<String, String>();
        result.putAll(res1);
        result.putAll(res2);

        // make a sorted list of changes
        var diffMap = result.entrySet()
                .stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .map(el -> {
                    if (el.getKey().endsWith("_m")) {
                        return "- " + el.getValue();
                    } else if (el.getKey().endsWith("_p")) {
                        return "+ " + el.getValue();
                    } else {
                        return "  " + el.getValue();
                    }
                })
                .toList();

        if (inputFormat.equals("stylish")) {
            return makeStylishDiff(diffMap);
        } else {
            return diffMap.toString();
        }
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

        var result = generate(filepath1, filepath2, format);

        System.out.println(result);

        return 0;
    }
}
