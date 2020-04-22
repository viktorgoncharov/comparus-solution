package de.comparus.opensource.longmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;

public class LongMapImplVersion2<V> implements LongMap<V> {
    public static final int TABLE_SIZE = 10;
    private Bucket[] table = new Bucket[TABLE_SIZE];
    private Long size = 0L;

    /**
     * Calculates the number of the cell in the table
     *
     * @param key
     * @return
     */
    private int calcId(long key) {
        final int id = abs((int) (key % TABLE_SIZE));
        return id;
    }

    @Override
    public V put(long key, V value) throws Exception {
        final Pair pair = new Pair(key, value);
        final int id = calcId(key);
        if (this.table[id] == null) {
            this.table[id] = new Bucket();
        }
        this.table[id].addItem(pair);
        this.size++;
        return value;
    }

    @Override
    public V get(long key) {
        final int id = calcId(key);
        if (this.table[id] == null) {
            return null;
        }
        final Optional<Pair<V>> pair = this.table[id].get(key);
        if (pair.isPresent()) {
            return pair.get().getValue();
        } else {
            return null;
        }
    }

    @Override
    public V remove(long key) {
        final int id = calcId(key);
        if (this.table[id] == null) {
            return null;
        }
        final Optional<Pair<V>> pair = this.table[id].get(key);
        if (pair.isPresent()) {
            V value = pair.get().getValue();
            this.table[id].remove(key);
            if (this.table[id].isEmpty()) {
                this.table[id] = null;
            }
            this.size--;
            return value;
        } else {
            return null;
        }
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0L;
    }

    @Override
    public boolean containsKey(long key) {
        int id = calcId(key);
        final Bucket bucket = this.table[id];
        if (bucket == null) {
            return false;
        }
        final Optional<Pair<V>> pair = bucket.get(key);
        if (pair.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        for (int i=0;i<table.length;i++) {
            final Bucket bucket = this.table[i];
            if (bucket!=null && bucket.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public long[] keys() {
        final List<Long> keys = new ArrayList<>();
        for (int i=0;i<this.size;i++) {
            if (this.table[i] != null) {
                keys.addAll(this.table[i].getKeys());
            }
        }
        return keys.stream().mapToLong(l->l).toArray();
    }

    @Override
    public V[] values() {
        final List<V> values = new ArrayList<>();
        for (int i=0;i<this.size;i++) {
            if (this.table[i] != null) {
                values.addAll(this.table[i].getValues());
            }
        }
        @SuppressWarnings("unchecked")
        V[] res = (V[])new Object[(int)(long)this.size];
        for (int i=0;i<this.size;i++) {
            res[i] = values.get(i);
        }
        return res;
    }

    @Override
    public long size() {
        return this.size;
    }

    @Override
    public void clear() {
        this.table = new Bucket[TABLE_SIZE];
        this.size = 0L;
    }

    public int count() {
        int total = 0;
        for (int i=0;i<this.table.length;i++) {
            Bucket bucket = this.table[i];
            if (bucket != null) {
                total+=bucket.size();
            }
        }
        return total;
    }
}