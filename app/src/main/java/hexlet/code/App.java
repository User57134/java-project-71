package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.concurrent.Callable;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "checksum 4.0",
        description = "Compares two configuration files and shows a difference.")
class Differ implements Callable<Integer> {

//    @Parameters(index = "0", description = "The file whose checksum to calculate.")
//    private File file;

    @Option(names = { "-h", "--help" }, usageHelp = true, description = "Show this help message and exit.")
    private boolean help = false;

    @Option(names = { "-V", "--version" }, description = "Print version information and exit.")
    private boolean version = false;

    @Override
    public Integer call() throws Exception { // your business logic goes here...
//        byte[] fileContents = Files.readAllBytes(file.toPath());
//        byte[] digest = MessageDigest.getInstance(algorithm).digest(fileContents);
//        System.out.printf("%0" + (digest.length * 2) + "x%n", new BigInteger(1, digest));
        return 0;
    }
}



public class App {

    public static void main(String[] args) {
        //System.out.println("Hello World!");

        int exitCode = new CommandLine(new Differ()).execute(args);
        System.exit(exitCode);
    }
}