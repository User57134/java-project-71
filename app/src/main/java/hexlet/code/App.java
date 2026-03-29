package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "checksum 4.0",
        description = "Compares two configuration files and shows a difference.")
class Differ implements Callable<Integer> {
    private static ObjectMapper mapper = new ObjectMapper();

    private static Map<String,Object> getData(String filename) throws Exception {
        var p = Paths.get(filename);
        var content = Files.readString(p);

        if ((content == null) || (content.isEmpty())) {
            throw new RuntimeException("Invalid argument: " + filename);
        }

        return mapper.readValue(content, new TypeReference<Map<String,Object>>(){});
    }

    private static String diffAsString(List<String> diff) {
        StringBuilder builder = new StringBuilder();

        builder.append("{");
        builder.append("\n");

        for (var line : diff) {
            builder.append(line);
            builder.append("\n");
        }

        builder.append("}");

        return builder.toString();
    }


    @Parameters(paramLabel = "filepath1", defaultValue = "", description = "path to first file")
    private String filepath1;

    @Parameters(paramLabel = "filepath2", defaultValue = "", description = "path to second file")
    private String filepath2;

    @Option(names = { "-h", "--help" }, usageHelp = true, description = "Show this help message and exit")
    private boolean help = false;

    @Option(names = { "-V", "--version" }, description = "Print version information and exit")
    private boolean version = false;

    @Option(names = {"-f", "--format"}, paramLabel = "FORMAT", description = "output format [default: stylish]")
    private String format = "stylish ";

    @Override
    public Integer call() throws Exception { // your business logic goes here...
        //String filename1 = "src/main/resources/file1.json";
        //String filename2 = "src/main/resources/file2.json";

        var content1 = getData(filepath1);
        var content2 = getData(filepath2);

        // lines, that were removed and lines with no changes
        var res1 = content1.entrySet()
                .stream()
                // make a map with a surrogate keys (_m) to sort it by value with the special rules:
                // - first is a line without changes
                // - second is a removed line
                // - third is an added line
                .collect(Collectors.toMap(el -> {
                    if ((content2.containsKey(el.getKey()))
                            && (el.getValue().equals(content2.get(el.getKey())))) {
                        return el.getKey();
                    } else {
                        return el.getKey() + "_m";
                    }

                }, el -> el.getKey() + ": " + el.getValue()));

        // lines, that were added
        var res2 = content2
                .entrySet()
                .stream()
                .filter(el -> {
                    if ((!content1.containsKey(el.getKey()))
                            || (!el.getValue().equals(content1.get(el.getKey())))) {
                        return true;
                    } else {
                        return false;
                    }
                })
                // make a map with a surrogate keys (_p) to sort it by value with the special rules:
                // - first is a line without changes
                // - second is a removed line
                // - third is an added line
                .collect(Collectors.toMap(el -> {
                    return el.getKey() + "_p";
                }, el -> el.getKey() + ": " + el.getValue()));

        var result = new HashMap<String, String>();
        result.putAll(res1);
        result.putAll(res2);

        // make a sorted list of changes
        var diff = result.entrySet()
                .stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .map( el -> {
                    if (el.getKey().endsWith("_m")) {
                        return "- " + el.getValue();
                    } else if (el.getKey().endsWith("_p")) {
                        return "+ " + el.getValue();
                    } else {
                        return "  " + el.getValue();
                    }
                })
                .toList();

        var sdiff = diffAsString(diff);

        System.out.println(sdiff);

        return 0;
    }
}


public class App {

    public static void main(String[] args) {
        Path currentPath = Paths.get("").toAbsolutePath();
        System.out.println("Current working directory: " + currentPath);

        int exitCode = new CommandLine(new Differ()).execute(args);
        System.exit(exitCode);
    }
}