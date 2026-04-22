package hexlet.code;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.util.Map;

public final class Parser {

    private Parser() {

    }

    public static Map<String, Object> parse(String text, ContentFormat contentFormat) throws JsonProcessingException {
        ObjectMapper mapper = null;

        switch (contentFormat) {
            case JSON:
                mapper = new ObjectMapper();
                break;
            case YAML:
                mapper = new YAMLMapper();
                break;
            default:
                throw new RuntimeException("Unknown format: " + contentFormat);
        }

        return mapper.readValue(text, new TypeReference<Map<String, Object>>() { });
    }
}
