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
import java.util.Map;
import java.util.concurrent.Callable;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "checksum 4.0",
        description = "Compares two configuration files and shows a difference.")
class Differ implements Callable<Integer> {
    private static ObjectMapper mapper = new ObjectMapper();

    public static Map<String,Object> getData(String filename) throws Exception {
        var p = Paths.get(filename);
        var content = Files.readString(p);

        if ((content == null) || (content.isEmpty())) {
            throw new RuntimeException("Invalid argument: " + filename);
        }

        return mapper.readValue(content, new TypeReference<Map<String,Object>>(){});
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
        String filename1 = "src/main/resources/file1.json";
        String filename2 = "src/main/resources/file2.json";

        var content1 = getData(filename1);
        var content2 = getData(filename2);

        System.out.println((content1.equals(content2)) ? "files are equal" : "files are not equal");

        return null;
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