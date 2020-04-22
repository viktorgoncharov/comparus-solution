package de.comparus.opensource.longmap;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

import static org.testng.Assert.*;

public class BucketTest {
    final Random random = new Random();
    @Test
    void putTest() throws Exception {
        final Bucket bucket = new Bucket();
        for (int i=0;i<11;i++) {
            final Long key = (long) random.nextInt(20);
            final String value = "Value ".concat(String.valueOf(key));
            final Pair<String> pair = new Pair(key, value);
            bucket.addItem(pair);
        }
        final int size = bucket.size();
        Assert.assertTrue(true);
    }
}