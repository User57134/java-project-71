package hexlet.code;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.SortedMap;
import java.util.TreeMap;


enum FileType {
    JSON,
    YAML
}


public final class Differ  {
    public static final String DEFAULT_OUT_FORMAT = "stylish";

    private Differ() {

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


    private static FileType defineFileType(String filename1, String filename2) {
        FileType fileType = null;

        String extension = getFileExtension(filename1);
        if ((extension != null) && (extension.equals(getFileExtension(filename2)))) {
            if (extension.equals("json")) {
                fileType = FileType.JSON;
            } else if (extension.equals("yml")) {
                fileType = FileType.YAML;
            }
        }

        return fileType;
    }


    private static String viewDiffAs(SortedMap<String, TreeMap<String, Object>> differences,
                                     String format) throws Exception {

        var formatter = Formatters.getFormatter(format);

        return formatter.format(differences);
    }


    private static String getFileText(String filename) throws IOException {
        var p = Paths.get(filename);

        return Files.readString(p);
    }


    public static String generate(String filename1, String filename2) throws Exception {
        return generate(filename1, filename2, DEFAULT_OUT_FORMAT);
    }


    public static String generate(String filename1, String filename2, String viewFormat) throws Exception {
        FileType fileType = defineFileType(filename1, filename2);

        if (fileType == null) {
            throw new Exception("Wrong input files: unknown extension.");
        }

        String text1 = getFileText(filename1);

        if ((text1 == null) || (text1.isEmpty())) {
            throw new Exception("Wrong input data: " + filename1 + " has no data.");
        }

        String text2 = getFileText(filename2);

        if ((text2 == null) || (text2.isEmpty())) {
            throw new Exception("Wrong input data: " + filename2 + " has no data.");
        }

        Parser parser = new Parser(fileType);

        var content1 = parser.parse(text1);
        var content2 = parser.parse(text2);

        var result = DifferenceAnalyzer.analyze(content1, content2);

        return viewDiffAs(result, viewFormat);
    }
}
