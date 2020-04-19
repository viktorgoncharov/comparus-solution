package de.comparus.opensource.longmap.avl;

import com.beust.jcommander.internal.Lists;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class NodeTest {
    public static final int MAX_ITEMS = 10;
    private Random random = new Random();

    @Test
    void testBalanced() throws Exception {
        Node<String> node = new Node<>(0L,"Root");
/*
        final List<Integer> keys = new ArrayList<>();
        IntStream.range(1, MAX_ITEMS).forEach(item -> keys.add(item));
*/
        final List<Integer> keys = Lists.newArrayList(0,7,2,1,4,6,3,8,5,9);
//        final List<Integer> keys = Lists.newArrayList(0,5,1,3,4,2,9,7,6,8);
//        final List<Integer> keys = Lists.newArrayList(0,1,3,4,6,5,7,9);
        for (int i=1;i<MAX_ITEMS;i++) {
//            int keyIndex = random.nextInt(keys.size());
            int keyIndex = i;
            Long key = Long.valueOf(keys.get(keyIndex));
//            keys.remove(keyIndex);
            System.out.println(key);
            String value = "Node #".concat(String.valueOf(key));
            node = node.insert(key, value);
        }
        Assert.assertTrue(true);
    }
}