package binance.seekers;

import binance.config.IConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class WsProfitSeeker extends DefaultProfitSeeker {
    private final static Logger logger = LoggerFactory.getLogger(IConfigLoader.class);

    private final Map<String, Object> bidsAsks;

    public WsProfitSeeker() {
        bidsAsks = new HashMap<>();
        bidsAsks.put("bids", new ArrayList<>());
        bidsAsks.put("asks", new ArrayList<>());
        bidsAsks.put("lastUpdateId", -1);
    }

    @Override
    public String handle(Map<String, Object> bidsAsksUpdate) {
        if (!bidsAsks.get("lastUpdateId").equals(bidsAsksUpdate.get("lastUpdateId"))) {
            merge((List<List<Object>>) bidsAsksUpdate.get("bids"),
                    (List<List<Object>>) bidsAsks.get("bids"));
            merge((List<List<Object>>) bidsAsksUpdate.get("asks"),
                    (List<List<Object>>) bidsAsks.get("asks"));

            bidsAsks.replace("lastUpdateId", bidsAsksUpdate.get("lastUpdateId"));
        }

        return super.handle(bidsAsks);
    }

    private void merge(List<List<Object>> update, List<List<Object>> target) {
        for (List<Object> element : update) {
            int index = Collections.binarySearch(target,
                    element,
                    (a, b) -> Double.valueOf((String) b.get(0))
                            .compareTo(Double.valueOf((String) a.get(0)))
            );

            boolean quantityEqualsZero = Double.valueOf((String) element.get(1)).equals(0.0);
            if (index >= 0) {
                if (quantityEqualsZero) {
                    logger.debug("Remove element with price {} and zero quantity", element.get(0));
                    target.remove(index);
                } else {
                    logger.debug("Replace element with price {}", element.get(0));
                    target.set(index, element);
                }
            } else {
                if (!quantityEqualsZero) {
                    logger.debug("Add element with price {}", element.get(0));
                    target.add(Math.abs(index + 1), element);
                }
            }
        }

    }
}
