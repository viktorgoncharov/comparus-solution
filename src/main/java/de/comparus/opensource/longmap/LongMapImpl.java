package de.comparus.opensource.longmap;

import de.comparus.opensource.longmap.avl.Node;

import java.util.Optional;

public class LongMapImpl<V> implements LongMap<V> {
    public static final int VALUES_INCREMENT = 1000;
    Node<Integer> node;
    private Object[] values = new Object[VALUES_INCREMENT];
    private int currentValue = 0;

    private void extendValues() {
        Object[] oldValues = this.values;
        this.values = new Object[oldValues.length + VALUES_INCREMENT];
        for (int i=0;i<oldValues.length;i++) {
            this.values[i] = oldValues[i];
        }
    }

    private void removeFromValues(int index) {
        for (int i=index;i<values.length;i++) {
            values[i] = values[i+1];
        }
        this.currentValue--;
    }

    private boolean valuesIsEmpty() {
        return this.currentValue == this.values.length;
    }

    private int addValue(V value) {
        if (valuesIsEmpty()) {
            extendValues();
        }
        this.values[this.currentValue] = value;
        this.currentValue++;
        return this.currentValue-1;
    }

    public V put(long key, V value) throws Exception {
        final int index = addValue(value);
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
        // TODO: 20.04.20
        return null;
    }

    public V[] values() {
        return (V[])this.values;
    }

    public long size() {
        return this.currentValue;
    }

    public void clear() {
        this.values = new Object[VALUES_INCREMENT];
        this.node = null;
    }
}
