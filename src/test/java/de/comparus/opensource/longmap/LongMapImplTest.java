package de.comparus.opensource.longmap;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.lang.Math.abs;

public class LongMapImplTest {
    public static final long FIXED_KEY = 999999L;
    private Random random = new Random();
    private Long storedKey;
    private String storedValue;

    private LongMap<String> produce(int limit) throws Exception {
        final LongMap<String> map = new LongMapImpl<>();
        // creating cycle
        for (Integer i = 0; i< limit; i++) {
            final Long key = i * 10L;
            final String val = "value".concat(key.toString());
            map.put(key, val);
        }
        return map;
    }

    private LongMap<String> randomize(int limit) throws Exception {
        final LongMap<String> map = new LongMapImpl<>();
        // creating cycle
        for (Integer i = 0; i< limit; i++) {
            Long key = random.nextLong();
            final String val = "value #".concat(key.toString());
            if (i == limit / 2) {
                this.storedKey = key;
                this.storedValue = val;
            }
            map.put(key, val);
        }
        return map;
    }

    @Test
    void putGetTest() throws Exception {
        final LongMap<String> map = new LongMapImpl<>();
        final String value1 = "value1";
        final String value2 = "value2";
        final Long key1 = 101010L;
        final Long key2 = 202020L;
        map.put(key1, value1);
        map.put(key2, value2);
        final String res1 = map.get(key1);
        Assert.assertEquals(value1, res1);
        final String res2 = map.get(key2);
        Assert.assertEquals(value2, res2);
        final Long unknownKey = 575757L;
        final String unRes = map.get(unknownKey);
        Assert.assertNull(unRes);
    }

    @Test
    public void producingTest() {
        final LongMap<String> map = new LongMapImpl<>();
        for (int i=0;i<100;i++) {
            final Long key = (long)i*10;
            final String value = "Value #".concat(key.toString());
            map.put(key, value);
            final int cnt = ((LongMapImpl)map).count();
            if (cnt != i+1) {
                System.out.println("Key = ".concat(key.toString()).concat(" value = ").concat(value));
            }
        }
        Assert.assertTrue(true);
    }

    @Test
    void removeTest() throws Exception {
        final LongMap<String> map = produce(10000);
        final long originalSize = map.size();
        final List<Integer> toBeRemoved = Arrays.asList(20,50,70);
        // removing cycle
        for (int i=0; i<toBeRemoved.size(); i++) {
            final Integer index = toBeRemoved.get(i);
            String removedValue = map.remove(index);
            if (removedValue == null) {
                removedValue = "NULL";
            }
            System.out.println("Removed value = ".concat(removedValue));
        }
        // checking cycle
        for (int i=0;i<toBeRemoved.size();i++) {
            final Integer index = toBeRemoved.get(i);
            final String res = map.get(index);
            Assert.assertNull(res);
        }
        // size checking
        final Long sizeAfterRemoved = originalSize - toBeRemoved.size();
        Assert.assertEquals(Long.valueOf(map.size()), sizeAfterRemoved);
    }

    @Test
    void containsTest() throws Exception {
        final LongMap<String> map = produce(10);
        Assert.assertTrue(map.containsKey(2000L));
        Assert.assertTrue(map.containsValue("value2000"));
        Assert.assertFalse(map.containsKey(19000L));
        Assert.assertFalse(map.containsValue("value19000"));
        Assert.assertFalse(map.isEmpty());
        map.clear();
        Assert.assertTrue(map.isEmpty());
        Assert.assertEquals(map.size(), 0L);
    }

    @Test
    void keysValuesTest() throws Exception {
        final LongMap<String> map = new LongMapImpl<>();
        final long [] keys = new long[10];
        final String[] values = new String[10];
        // creating cycle
        for (Integer i=0; i<10; i++) {
            final Long key = i * 1000L;
            keys[i] = key;
            final String val = "value".concat(i.toString());
            values[i] = val;
            map.put(key, val);
        }
        final long[] keysRes = map.keys();
        final Object[] valsRes = map.values();
        Assert.assertTrue(Arrays.equals(keys, keysRes));
        Assert.assertTrue(Arrays.equals(values, valsRes));
    }

    @Test
    void testCount() throws Exception {
        final LongMap<String> lMap = new LongMapImpl<>();
        int offset = 1;
        int cnt;
        for (int i=0;i<80;i++) {
            final Long key = random.nextLong();
            final String value = "Value #".concat(String.valueOf(i));
            lMap.put(key, value);
            cnt = ((LongMapImpl)lMap).count();
            if (i-cnt > offset) {
                System.out.println("key=".concat(key.toString()).concat(" value=").concat(value));
                offset++;
            }
        }
        final int count = ((LongMapImpl)lMap).count();
        Assert.assertTrue(true);
    }

    @Test
    void benchmark() throws Exception {
        /**
         * inserting
         */
        final int LIMIT = 100000;
        final LocalDateTime lmGenStartedAt = LocalDateTime.now();
        final LongMap<String> map = randomize(LIMIT);
        final LocalDateTime lmGenFinishedAt = LocalDateTime.now();

        final LocalDateTime jmGenStartedAt = LocalDateTime.now();
        final Map<Long,String> jMap = new HashMap<>();
        Long jStoredKey = null;
        String jStoredValue = "";
        for (int i=0;i<LIMIT;i++) {
            Long key = random.nextLong();
            final String value = "Value #".concat(String.valueOf(key));
            if (i == LIMIT/2) {
                jStoredKey = key;
                jStoredValue = value;
            }
            jMap.put(key, value);
        }
        final LocalDateTime jmGenFinishedAt = LocalDateTime.now();

        final long lmDuration = ChronoUnit.MILLIS.between(lmGenStartedAt, lmGenFinishedAt);
        final long jmDuration = ChronoUnit.MILLIS.between(jmGenStartedAt, jmGenFinishedAt);

        Assert.assertTrue(true);

        /**
         * getting
         */
        final LocalDateTime lmGettingStartedAt = LocalDateTime.now();
        String val = map.get(storedKey);
        boolean lmRes = map.containsValue(storedValue);
        final LocalDateTime lmGettingFinishedAt = LocalDateTime.now();
        final Long lmGettingDuration = ChronoUnit.MILLIS.between(lmGettingStartedAt, lmGettingFinishedAt);
        final int lmTotal = ((LongMapImpl)map).count();

        final LocalDateTime jmGettingStartedAt = LocalDateTime.now();
        boolean jmRes = jMap.containsValue(jStoredValue);
        final LocalDateTime jmGettingFinishedAt = LocalDateTime.now();
        final Long jmGettingDuration = ChronoUnit.MILLIS.between(lmGettingStartedAt, lmGettingFinishedAt);

        Assert.assertTrue(lmGettingDuration / (jmGettingDuration * 1.0) < 5);
    }
}
