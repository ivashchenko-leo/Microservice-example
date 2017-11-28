package company.seekers;

import java.util.Map;

/**
 * An interface for handlers that deals with parsed input
 * We can do anything with this data so it's a basic interface
 * @param <T> return type of a result after handling
 */
public interface IHandler<T> {
    T handle(final Map<String, Object> input);
}
