package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.util.Map;

public final class Parser {
    private ObjectMapper mapper = null;

    public Parser(FileType type) {
        switch (type) {
            case JSON:
                mapper = new ObjectMapper();
                break;
            case YAML:
                mapper = new YAMLMapper();
                break;
            default:
                throw new RuntimeException("Unknown file type: " + type);
        }
    }

    public Map<String, Object> parse(String content) throws Exception {
        return mapper.readValue(content, new TypeReference<Map<String, Object>>() { });
    }
}
