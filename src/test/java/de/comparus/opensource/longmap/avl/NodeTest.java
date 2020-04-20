package de.comparus.opensource.longmap.avl;

import com.beust.jcommander.internal.Lists;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.IntStream;

public class NodeTest {
    public static final int MAX_ITEMS = 10000;
    private Random random = new Random();

    private Node<String> produce10K() throws Exception {
        Node<String> node = new Node<>(0L,"Root");

        final List<Integer> keys = new ArrayList<>();
        IntStream.range(1, MAX_ITEMS).forEach(item -> keys.add(item));
        for (int i=1;i<MAX_ITEMS;i++) {
            int keyIndex = random.nextInt(keys.size());
            Long key = Long.valueOf(keys.get(keyIndex));
            keys.remove(keyIndex);
            String value = "Node #".concat(String.valueOf(key));
            node = node.insert(key, value);
        }
        return node;
    }

    @Test
    void testBalanced() throws Exception {
        Node<String> node = new Node<>(0L,"Root");
        final List<Integer> keys = Lists.newArrayList(0,7,2,1,4,6,3,8,5,9);
        for (int i=1;i<MAX_ITEMS;i++) {
            int keyIndex = i;
            Long key = Long.valueOf(keys.get(keyIndex));
            System.out.println(key);
            String value = "Node #".concat(String.valueOf(key));
            node = node.insert(key, value);
        }
        Assert.assertTrue(true);
    }

    /**
     *  The tree's height can't be higher than 19 according to theoretical limit is computed by the formula:
     *  1.45 * Log2(n+2), that equals 19 for a tree with 10K nodes
     *
     *  @throws Exception
     */
    @Test
    void checkTheoreticalLimit() throws Exception {
        final List<Integer> sizes = new ArrayList<>();
        for (int i=0;i<1000;i++) {
            System.out.println("Pass ".concat(String.valueOf(i)));
            final Node<String> node = produce10K();
            sizes.add((int)node.getHeight());
        }
        final IntSummaryStatistics stats = sizes.stream().mapToInt(x -> x).summaryStatistics();
        System.out.println("Max = ".concat(String.valueOf(stats.getMax())));
        System.out.println("Min = ".concat(String.valueOf(stats.getMin())));
        System.out.println("Avg = ".concat(String.valueOf(stats.getAverage())));
        Assert.assertTrue(stats.getMax() < 19);
    }
}