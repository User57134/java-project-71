package hexlet.code;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "gendiff", mixinStandardHelpOptions = true, version = "checksum 4.0",
        description = "Compares two configuration files and shows a difference.")
public final class App implements Callable<Integer>  {

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
        var result = Differ.generate(filepath1, filepath2, format);

        if (result != null) {
            System.out.println(result);
        }

        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}
