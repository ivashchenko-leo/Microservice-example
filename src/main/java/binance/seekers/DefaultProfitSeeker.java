package binance.seekers;

import binance.config.IConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * A handler that seeks for a profitable arbitrage opportunity.
 * @see IHandler
 */
public class DefaultProfitSeeker implements IHandler<String> {
    private final static Logger logger = LoggerFactory.getLogger(IConfigLoader.class);

    private Integer lastUpdateId = 0;


    /**
     * I'm not sure if I understood the task right
     * In general this method looks for bids which price is bigger than ask's price
     * @param bidsAsks Map with bids, asks and lastUpdateId
     * @return A string with prices and quantity of profitable bids and asks
     */
    @Override
    public String handle(Map<String, Object> bidsAsks) {
        if (lastUpdateId.equals(bidsAsks.get("lastUpdateId"))) {
            return "None";
        } else {
            lastUpdateId = (Integer) bidsAsks.get("lastUpdateId");
        }

        logger.debug("Handle data with lastUpdateId {}", lastUpdateId);
        List<List<Object>> bids = (List<List<Object>>) bidsAsks.get("bids");
        List<List<Object>> asks = (List<List<Object>>) bidsAsks.get("asks");

        StringBuilder bidsRelevantPrices = new StringBuilder();
        StringBuilder asksRelevantPrices = new StringBuilder();
        StringBuilder bidsRelevantQuantity = new StringBuilder();
        StringBuilder asksRelevantQuantity = new StringBuilder();
        double profit = 0.0;

        logger.debug("Analyze {} bids and {} asks", bids.size(), asks.size());
        outerloop:
        for (List<Object> bid : bids) {
            double bidPrice;
            double bidQuantity;
            //skip rows with bad values
            try {
                bidPrice = Double.valueOf((String)bid.get(0));
                bidQuantity = Double.valueOf((String)bid.get(1));
            } catch (Exception ex) {
                logger.debug("Bid has a bad value {} or {} lastUpdateId {}",
                        bid.get(0), bid.get(1), lastUpdateId);
                continue;
            }
            //or zero, less than zero values
            if (bidPrice <= 0.0 || bidQuantity <= 0.0) {
                logger.debug("Bid's price or quantity is zero or less, lastUpdateId {}", lastUpdateId);
                continue;
            }

            for (List<Object> ask : asks) {
                double askPrice;
                double askQuantity;
                try {
                    askPrice = Double.valueOf((String)ask.get(0));
                    askQuantity = Double.valueOf((String)ask.get(1));
                } catch (Exception ex) {
                    logger.debug("Ask has a bad value {} or {} lastUpdateId {}",
                            bid.get(0), bid.get(1), lastUpdateId);
                    continue;
                }

                if (askPrice <= 0.0 || askQuantity <= 0.0) {
                    logger.debug("Ask's price or quantity is zero or less, lastUpdateId {}", lastUpdateId);
                    continue;
                }

                //no point to look more
                if (bidPrice <= askPrice) {
                    break outerloop;
                }

                bidsRelevantPrices.append(bidPrice).append(" ");
                bidsRelevantQuantity.append(bidQuantity).append(" ");
                asksRelevantQuantity.append(askQuantity).append(" ");
                asksRelevantPrices.append(askPrice).append(" ");
                if (bidQuantity <= askQuantity) {
                    //that means we could use this ask for another bid to get some profit
                    ask.set(1, String.valueOf(askQuantity - bidQuantity));
                    profit += (bidPrice - askPrice) * bidQuantity;
                    break;
                } else {
                    //we can look for another ask for this bid
                    bidQuantity -= askQuantity;
                    bid.set(1, String.valueOf(bidQuantity));
                    ask.set(1, String.valueOf(0.0));
                    profit += (bidPrice - askPrice) * askQuantity;
                }
            }
        }

        if (profit == 0.0) {
            return "None";
        }

        return String.format("bids prices %s quantity %s - asks prices %s quantity %s profit %f",
                bidsRelevantPrices.toString(),
                bidsRelevantQuantity.toString(),
                asksRelevantPrices.toString(),
                asksRelevantQuantity.toString(),
                profit);
    }
}
