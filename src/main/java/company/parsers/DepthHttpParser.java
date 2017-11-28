package company.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Parser for json from http url
 * @see IParser
 */
public class DepthHttpParser implements IParser {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> parse(final byte[] input) throws Exception {
        return objectMapper.readValue(input, HashMap.class);
    }
}
