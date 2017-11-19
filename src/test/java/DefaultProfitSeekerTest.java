import binance.seekers.DefaultProfitSeeker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class DefaultProfitSeekerTest {

    private DefaultProfitSeeker seeker = new DefaultProfitSeeker();

    @Before
    public void before() {
        seeker = new DefaultProfitSeeker();
    }

    @Test
    public void oneRelevantBidTest() {
        Map<String, Object> input = new HashMap<>();
        List<Object> bid = new ArrayList<>();
        List<List<Object>> bids = new ArrayList<>();
        List<Object> ask = new ArrayList<>();
        List<List<Object>> asks = new ArrayList<>();

        //bid.add(0, "0.00021218");
        bid.add(0, "2");
        bid.add(1, "30.00000000");

        //ask.add(0, "0.00021208");
        ask.add(0, "1");
        ask.add(1, "15.00000000");

        bids.add(bid);
        asks.add(ask);
        input.put("bids", bids);
        input.put("asks", asks);
        input.put("lastUpdateId", 1);

        String result = seeker.handle(input);
        Assert.assertEquals("bids prices 2.0  quantity 30.0  - " +
                        "asks prices 1.0  quantity 15.0  profit 15.000000",
                result);
    }

    @Test
    public void noRelevantBidsOrAsksTest() {
        Map<String, Object> input = new HashMap<>();

        input.put("lastUpdateId", 1);
        List<List<Object>> bids = new ArrayList<>();
        List<List<Object>> asks = new ArrayList<>();

        //bid.add(0, "0.00021218");
        bids.add(Arrays.asList("1", "15.00000000"));
        asks.add(Arrays.asList("2", "30.00000000"));

        input.put("bids", bids);
        input.put("asks", asks);
        String result = seeker.handle(input);
        Assert.assertEquals("None", result);
    }

    @Test
    public void checkLastUpdateIdTest() {
        Map<String, Object> input = new HashMap<>();

        input.put("lastUpdateId", 1);
        List<List<Object>> bids = new ArrayList<>();
        List<List<Object>> asks = new ArrayList<>();

        //bid.add(0, "0.00021218");
        bids.add(Arrays.asList("2", "15.00000000"));
        asks.add(Arrays.asList("1", "30.00000000"));

        input.put("bids", bids);
        input.put("asks", asks);
        String result = seeker.handle(input);
        Assert.assertNotEquals("None", result);

        result = seeker.handle(input);
        Assert.assertEquals(
                "None",
                result);

        input.put("lastUpdateId", 2);
        bids.clear();
        asks.clear();

        bids.add(Arrays.asList("2", "15.00000000"));
        asks.add(Arrays.asList("1", "30.00000000"));
        result = seeker.handle(input);
        Assert.assertNotEquals("None", result);
    }

    @Test
    public void twoBidsOneAskTest() {
        Map<String, Object> input = new HashMap<>();
        List<List<Object>> bids = new ArrayList<>();
        List<List<Object>> asks = new ArrayList<>();

        bids.add(Arrays.asList("2", "15.00000000"));
        asks.add(Arrays.asList("1", "30.00000000"));

        bids.add(Arrays.asList("1.5", "12.00000000"));
        asks.add(Arrays.asList("2", "40.00000000"));

        input.put("bids", bids);
        input.put("asks", asks);
        input.put("lastUpdateId", 1);
        String result = seeker.handle(input);
        Assert.assertEquals("bids prices 2.0 1.5  quantity 15.0 12.0  - " +
                        "asks prices 1.0 1.0  quantity 30.0 15.0  profit 21.000000",
                result);
    }

    @Test
    public void oneBidTwoAsksTest() {
        Map<String, Object> input = new HashMap<>();
        List<List<Object>> bids = new ArrayList<>();
        List<List<Object>> asks = new ArrayList<>();

        bids.add(Arrays.asList("2", "30.00000000"));
        asks.add(Arrays.asList("1", "15.00000000"));

        bids.add(Arrays.asList("1.5", "12.00000000"));
        asks.add(Arrays.asList("1.5", "40.00000000"));

        input.put("bids", bids);
        input.put("asks", asks);
        input.put("lastUpdateId", 1);
        String result = seeker.handle(input);
        Assert.assertEquals("bids prices 2.0 2.0  quantity 30.0 15.0  - " +
                        "asks prices 1.0 1.5  quantity 15.0 40.0  profit 22.500000",
                result);
    }

    @Test
    public void noDataTest() {
        Map<String, Object> input = new HashMap<>();
        List<List<Object>> bids = new ArrayList<>();
        List<List<Object>> asks = new ArrayList<>();

        input.put("bids", bids);
        input.put("asks", asks);
        input.put("lastUpdateId", 1);
        String result = seeker.handle(input);
        Assert.assertEquals("None",
                result);
    }

    @Test
    public void badDataTest() {
        Map<String, Object> input = new HashMap<>();
        List<List<Object>> bids = new ArrayList<>();
        List<List<Object>> asks = new ArrayList<>();

        bids.add(Arrays.asList("-1.5", "12.00000000"));
        asks.add(Arrays.asList("-1.5", "40.00000000"));

        bids.add(Arrays.asList("2", "30.00000000"));
        asks.add(Arrays.asList("1", "15.00000000"));

        input.put("bids", bids);
        input.put("asks", asks);
        input.put("lastUpdateId", 1);
        String result = seeker.handle(input);
        Assert.assertEquals("bids prices 2.0  quantity 30.0  - " +
                        "asks prices 1.0  quantity 15.0  profit 15.000000",
                result);

        bids.clear();
        asks.clear();
        input.put("lastUpdateId", 2);

        bids.add(Arrays.asList("3.5", "-12.00000000"));
        asks.add(Arrays.asList("4.5", "00.00000000"));

        bids.add(Arrays.asList("2", "30.00000000"));
        asks.add(Arrays.asList("1", "15.00000000"));
        result = seeker.handle(input);
        Assert.assertEquals("bids prices 2.0  quantity 30.0  - " +
                        "asks prices 1.0  quantity 15.0  profit 15.000000",
                result);
    }
}
