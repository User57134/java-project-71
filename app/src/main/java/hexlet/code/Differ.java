package hexlet.code;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.SequencedMap;


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


    private static String viewDiffAs(SequencedMap<String, SequencedMap<String, Object>> differences,
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

        ObjectMapper mapper = null;
        switch (fileType) {
            case JSON:
                mapper = new ObjectMapper();
                break;
            case YAML:
                mapper = new YAMLMapper();
                break;
            default:
                throw new RuntimeException("Unknown file type: " + fileType);
        }

        var content1 = Parser.parse(text1, mapper);
        var content2 = Parser.parse(text2, mapper);

        var result = DifferenceAnalyzer.analyze(content1, content2);

        return viewDiffAs(result, viewFormat);
    }
}
