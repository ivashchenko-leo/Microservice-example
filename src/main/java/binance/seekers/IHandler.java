package binance.seekers;

import java.util.Map;

public interface IHandler<T> {
    T handle(final Map<String, Object> input);
}
