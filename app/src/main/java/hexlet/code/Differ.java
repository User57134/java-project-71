package hexlet.code;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;


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


    private static ContentFormat defineContentFormat(String filename1, String filename2) {
        ContentFormat contentFormat = null;

        String extension = getFileExtension(filename1);
        if ((extension != null) && (extension.equals(getFileExtension(filename2)))) {
            if (extension.equals("json")) {
                contentFormat = ContentFormat.JSON;
            } else if (extension.equals("yml")) {
                contentFormat = ContentFormat.YAML;
            }
        }

        return contentFormat;
    }


    private static String viewDiffAs(Map<String, LinkedHashMap<String, Object>> differences,
                                     String format) throws JsonProcessingException {

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


    public static String generate(String filename1, String filename2, String viewFormat) throws IOException {
        ContentFormat contentFormat = defineContentFormat(filename1, filename2);

        if (contentFormat == null) {
            throw new RuntimeException("Unknown format of input files.");
        }

        String text1 = getFileText(filename1);

        if ((text1 == null) || (text1.isEmpty())) {
            throw new RuntimeException("Wrong input data: " + filename1 + " has no data.");
        }

        String text2 = getFileText(filename2);

        if ((text2 == null) || (text2.isEmpty())) {
            throw new RuntimeException("Wrong input data: " + filename2 + " has no data.");
        }

        var content1 = Parser.parse(text1, contentFormat);
        var content2 = Parser.parse(text2, contentFormat);

        var result = DifferenceAnalyzer.analyze(content1, content2);

        return viewDiffAs(result, viewFormat);
    }
}
