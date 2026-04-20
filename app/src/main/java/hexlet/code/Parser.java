package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public final class Parser {
    private ObjectMapper mapper = null;

    public Parser(ObjectMapper oMapper) {
        mapper = oMapper;
    }

    public Map<String, Object> parse(String content) throws Exception {
        return mapper.readValue(content, new TypeReference<Map<String, Object>>() { });
    }
}
