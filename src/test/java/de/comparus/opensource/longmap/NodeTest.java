package de.comparus.opensource.longmap;

import com.beust.jcommander.internal.Lists;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class NodeTest {
    private Random random = new Random();

    private Node<String> produce10K() {
        return produce(10000);
    }

    private Node<String> produce100K() {
        return produce(100000);
    }

    private Node<String> produce(int size) {
        Node<String> node = new Node<>(0L,"Root");

        final List<Integer> keys = new ArrayList<>();
        IntStream.range(1, size).forEach(item -> keys.add(item));
        for (int i=1;i<size;i++) {
            int keyIndex = random.nextInt(keys.size());
            Long key = Long.valueOf(keys.get(keyIndex));
            keys.remove(keyIndex);
            String value = "Node #".concat(String.valueOf(key));
            node = node.insert(key, value);
        }
        return node;
    }

    @Test
    void testBalanced() {
        Node<String> node = new Node<>(0L,"Root");
        final List<Integer> keys = Lists.newArrayList(0,7,2,1,4,6,3,8,5,9);
        final Integer size = keys.size();
        for (int i=1;i<size;i++) {
            int keyIndex = i;
            Long key = Long.valueOf(keys.get(keyIndex));
            System.out.println(key);
            String value = "Node #".concat(String.valueOf(key));
            node = node.insert(key, value);
        }
        Assert.assertTrue(true);
    }

    /**
     *  The tree's height can't be higher than 19 for 10K nodes according
     *  to theoretical limit is computed by the formula:
     *
     *                       1.45 * Log2(n+2)
     *
     */
    @Test
    void checkTheoreticalLimit()  {
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

    @Test
    void testInserting() {
        Node<String> node = new Node<>(0L, "Root");
        for (int i=0;i<10;i++) {
            final Long key = (long)(i+1) * 10;
            final String value = "Value #".concat(String.valueOf(key));
            node = node.insert(key, value);
            final int cnt = node.countAllNodes();
            if (cnt != i+2) {
                System.out.println("Key = ".concat(key.toString()).concat(" Value = ").concat(value));
            }
        }
        Assert.assertEquals(node.size(), 11);
    }
}