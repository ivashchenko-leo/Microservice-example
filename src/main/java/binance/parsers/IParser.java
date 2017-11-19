package binance.parsers;

import java.util.Map;

public interface IParser {
    Map<String, Object> parse(final byte[] input) throws Exception;
}
