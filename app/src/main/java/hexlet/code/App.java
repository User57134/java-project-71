package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

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
    @Parameters(paramLabel = "filepath1", description = "path to first file")
    private File filepath1;

    @Parameters(paramLabel = "filepath2", description = "path to second file")
    private File filepath2;

    @Option(names = { "-h", "--help" }, usageHelp = true, description = "Show this help message and exit")
    private boolean help = false;

    @Option(names = { "-V", "--version" }, description = "Print version information and exit")
    private boolean version = false;

    @Option(names = {"-f", "--format"}, paramLabel = "FORMAT", description = "output format [default: stylish]")
    private String format = "stylish ";

    @Override
    public Integer call() throws Exception { // your business logic goes here...

        return 0;
    }
}



public class App {

    public static void main(String[] args) {

        int exitCode = new CommandLine(new Differ()).execute(args);
        System.exit(exitCode);
    }
}