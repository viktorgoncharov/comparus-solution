package de.comparus.opensource.longmap;

public class Pair<V> {
    private Long key;
    private V value;

    public Pair(Long key, V value) {
        this.key = key;
        this.value = value;
    }

    public Long getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
