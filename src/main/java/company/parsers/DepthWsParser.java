package company.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Parser for json from web socket url
 * @see IParser
 */
public class DepthWsParser implements IParser {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> parse(final byte[] input) throws Exception {
        //uhh it's an update, then we can't use DefaultProfitSeeker
        Map<String, Object> parsed = objectMapper.readValue(input, HashMap.class);
        parsed.put("lastUpdateId", parsed.get("u"));
        parsed.put("bids", parsed.get("b"));
        parsed.put("asks", parsed.get("a"));

        return parsed;
    }
}
