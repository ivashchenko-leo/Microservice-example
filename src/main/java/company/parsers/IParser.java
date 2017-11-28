package company.parsers;

import java.util.Map;

/**
 * An interface for parsers. Implementation depends on type of data.
 * We can create implementation for csv, json, xml, etc.
 */
public interface IParser {
    /**
     *
     * @param input data from a data source
     * @return Map of Objects. There is no point to use POJO because it's a basic interface.
     * @throws Exception if parsing is failed
     */
    Map<String, Object> parse(final byte[] input) throws Exception;
}
