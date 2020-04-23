package de.comparus.opensource.longmap;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.testng.Assert.*;

public class BucketTest {
    public static final int MAX = 15;
    final Random random = new Random();
    @Test
    void putTest() throws Exception {
        final Bucket bucket = new Bucket();
        final List<Integer> ids = IntStream.range(0,MAX).boxed().collect(Collectors.toList());
        for (int i = 0; i< MAX; i++) {
            final int index = random.nextInt(ids.size());
            final Long key = (long) ids.get(index);
            ids.remove(index);
            final String value = "Value ".concat(String.valueOf(key));
            final Pair<String> pair = new Pair(key, value);
            bucket.addItem(pair);
        }
        final int size = bucket.size();
        Assert.assertEquals(size, MAX);
    }
}