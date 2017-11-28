package company.seekers;

import company.config.IConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Extends DefaultProfitSeeker handler because in the web socket connection
 * we receive just an update for bids and asks so we have to merge this update to previous data
 * @see DefaultProfitSeeker
 */
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

    /**
     * Checks if update's elements are inside of target if it's so then removes
     * them (if a price is zero) or replaces with a new value. If target doesn't contain
     * update's elements then adds it.
     * @param update bids or asks to add, replace, remove
     * @param target all bids or asks received until now
     */
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
