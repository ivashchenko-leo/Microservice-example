package binance.seekers;

import java.util.List;
import java.util.Map;

public class DefaultProfitSeeker implements IHandler<String> {

    private Integer lastUpdateId = 0;

    @Override
    public String handle(Map<String, Object> bidsAsks) {
        if (lastUpdateId.equals(bidsAsks.get("lastUpdateId"))) {
            return "None";
        } else {
            lastUpdateId = (Integer) bidsAsks.get("lastUpdateId");
        }

        List<List<Object>> bids = (List<List<Object>>) bidsAsks.get("bids");
        List<List<Object>> asks = (List<List<Object>>) bidsAsks.get("asks");

        StringBuilder bidsRelevantPrices = new StringBuilder();
        StringBuilder asksRelevantPrices = new StringBuilder();
        StringBuilder bidsRelevantQuantity = new StringBuilder();
        StringBuilder asksRelevantQuantity = new StringBuilder();
        double profit = 0.0;

        outerloop:
        for (List<Object> bid : bids) {
            double bidPrice;
            double bidQuantity;
            //skip rows with bad values
            try {
                bidPrice = Double.valueOf((String)bid.get(0));
                bidQuantity = Double.valueOf((String)bid.get(1));
            } catch (Exception ex) {
                continue;
            }
            //or zero, less than zero values
            if (bidPrice <= 0.0 || bidQuantity <= 0.0) {
                continue;
            }

            for (List<Object> ask : asks) {
                double askPrice;
                double askQuantity;
                try {
                    askPrice = Double.valueOf((String)ask.get(0));
                    askQuantity = Double.valueOf((String)ask.get(1));
                } catch (Exception ex) {
                    continue;
                }


                if (askPrice <= 0.0 || askQuantity <= 0.0) {
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
                    ask.set(1, String.valueOf(askQuantity - bidQuantity));
                    profit += (bidPrice - askPrice) * bidQuantity;
                    break;
                } else {
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
