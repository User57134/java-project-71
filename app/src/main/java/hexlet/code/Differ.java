package hexlet.code;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


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
        FileType format = null;

        String extension = getFileExtension(filename1);
        if ((extension != null) && (extension.equals(getFileExtension(filename2)))) {
            if (extension.equals("json")) {
                format = FileType.JSON;
            } else if (extension.equals("yml")) {
                format = FileType.YAML;
            }
        }

        return format;
    }


    private static String viewDiffAs(Map<String, HashMap<String, Object>> differences, String format) throws Exception {
        var formatter = Formatters.getFormatter(format);

        return formatter.format(differences);
    }


    private static String getFileText(String filename) {
        var p = Paths.get(filename);

        String content = null;
        try {
            content = Files.readString(p);
        } catch (IOException e) {
            // null is returned
        }

        return content;
    }


    private static ObjectMapper getMapper(FileType type) throws Exception {
        ObjectMapper mapper = null;

        switch (type) {
            case JSON:
                mapper = new ObjectMapper();
                break;
            case YAML:
                mapper = new YAMLMapper();
                break;
            default:
                throw new Exception("Unknown file type: " + type);

        }

        return mapper;
    }


    private static Map<String, HashMap<String, Object>> analyzeDifferences(Map<String, Object> content1,
                                                                           Map<String, Object> content2) {
        // make a map with a Map<String, Object> as value:
        // - first is a line without changes
        // - second is a removed line
        var res1 = content1.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        el -> {
                            var result = new HashMap<String, Object>();
                            String key = el.getKey();
                            Object value = el.getValue();

                            if ((content2.containsKey(key))
                                    &&
                                    (((value != null) && (value.equals(content2.get(key))))
                                            || ((value == null) && (content2.get(key) == null)))) {
                                result.put("=", value);
                            } else {
                                result.put("-", value);
                            }
                            return result; },
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        }));

        // make a map with a Map<String, Object> as value:
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
                        Map.Entry::getKey,
                        el -> {
                            var result = new HashMap<String, Object>();
                            result.put("+", el.getValue());
                            return result; }));

        // make a map with a Map<String, Object> as value:
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

        return result;
    }


    public static String generate(String filename1, String filename2) throws Exception {
        return generate(filename1, filename2, DEFAULT_OUT_FORMAT);
    }


    public static String generate(String filename1, String filename2, String viewFormat) throws Exception {
        FileType type = defineFileType(filename1, filename2);

        if (type == null) {
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

        Parser parser = new Parser(getMapper(type));

        var content1 = parser.parse(text1);
        var content2 = parser.parse(text2);

        var result = analyzeDifferences(content1, content2);

        return viewDiffAs(result, viewFormat);
    }
}
