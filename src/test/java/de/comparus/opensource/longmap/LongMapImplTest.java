package de.comparus.opensource.longmap;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class LongMapImplTest {

    private LongMap<String> create10() throws Exception {
        final LongMap<String> map = new LongMapImpl<>();
        // creating cycle
        for (Integer i = 0; i< 10; i++) {
            final Long key = i * 1000L;
            final String val = "value".concat(i.toString());
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
    void removeTest() throws Exception {
        final LongMap<String> map = create10();
        final List<Integer> toBeRemoved = Arrays.asList(2,5,7);
        // removing cycle
        for (int i=0; i<toBeRemoved.size(); i++) {
            final Integer index = toBeRemoved.get(i);
            map.remove(index);
        }
        // checking cycle
        for (int i=0;i<toBeRemoved.size();i++) {
            final Integer index = toBeRemoved.get(i);
            final String res = map.get(index);
            Assert.assertNull(res);
        }
        // size checking
        final Long sizeAfterRemoved = map.size() - toBeRemoved.size();
        Assert.assertEquals(Long.valueOf(map.size()), sizeAfterRemoved);
    }

    @Test
    void containsTest() throws Exception {
        final LongMap<String> map = create10();
        Assert.assertTrue(map.containsKey(2000L));
        Assert.assertTrue(map.containsValue("value5"));
        Assert.assertFalse(map.containsKey(9000L));
        Assert.assertFalse(map.containsValue("value9"));
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
        final String[] valsRes = map.values();
        Assert.assertTrue(Arrays.equals(keys, keysRes));
        Assert.assertTrue(Arrays.equals(values, valsRes));
    }
}
