package de.comparus.opensource.longmap;

import de.comparus.opensource.longmap.avl.Node;

import java.util.Optional;

public class LongMapImpl<V> implements LongMap<V> {
    public static final int INCREMENT = 1000;
    Node<Integer> node;
    private long[] keys = new long[INCREMENT];
    private Object[] values = new Object[INCREMENT];
    private int currentValue = 0;

    private void extendValues() {
        long[] oldKeys = this.keys;
        this.keys = new long[oldKeys.length + INCREMENT];
        Object[] oldValues = this.values;
        this.values = new Object[oldValues.length + INCREMENT];
        for (int i=0;i<oldValues.length;i++) {
            this.keys[i] = oldKeys[i];
            this.values[i] = oldValues[i];
        }
    }

    private void removeFromValues(int index) {
        for (int i=index;i<this.values.length;i++) {
            this.keys[i] = this.keys[i+1];
            this.values[i] = this.values[i+1];
        }
        this.currentValue--;
    }

    private boolean valuesIsEmpty() {
        return this.currentValue == this.values.length;
    }

    private int addValue(long key, V value) {
        if (valuesIsEmpty()) {
            extendValues();
        }
        this.values[this.currentValue] = value;
        this.keys[this.currentValue] = key;
        this.currentValue++;
        return this.currentValue-1;
    }

    public V put(long key, V value) throws Exception {
        final int index = addValue(key, value);
        if (this.node == null) {
            this.node = new Node<>(key, index);
        } else {
            this.node.insert(key, index);
        }
        return value;
    }

    public V get(long key) {
        if (this.node == null) {
            return null;
        }
        final Optional<Node<Integer>> found = this.node.get(key);
        if (!found.isPresent()) {
            return null;
        }
        int index = found.get().getValue();
        return (V)this.values[index];
    }

    public V remove(long key) {
        if (this.node == null) {
            return null;
        }
        final Optional<Node<Integer>> found = this.node.get(key);
        if (!found.isPresent()) {
            return null;
        }
        final int index = found.get().getValue();
        final V removed = (V)this.values[index];
        removeFromValues(index);
        this.node.remove(key);
        return removed;
    }

    public boolean isEmpty() {
        return this.node == null;
    }

    public boolean containsKey(long key) {
        final Optional<Node<Integer>> gotten = this.node.get(key);
        return gotten.isPresent();
    }

    public boolean containsValue(V value) {
        // TODO: 20.04.20
        return false;
    }

    public long[] keys() {
        return this.keys;
    }

    public V[] values() {
        return (V[])this.values;
    }

    public long size() {
        return this.currentValue;
    }

    public void clear() {
        this.values = new Object[INCREMENT];
        this.node = null;
    }
}
