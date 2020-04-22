package de.comparus.opensource.longmap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Item of the map
 * Can contain only one item or a list, or an AVL tree if total count
 * of pairs exceeds the maximum limit for list (LIST_LIMIT const)
 *
 * @param <V>
 */
public class Bucket<V> {
    public static final int LIST_LIMIT = 10;
    private BucketMode mode;
    private Pair<V> single;
    private List<Pair<V>> list;
    private Node<V> node;

    private static int entered = 0;

    public void addItem(Pair<V> pair) throws Exception {
        if (this.mode == null) {
            this.single = pair;
            this.mode = BucketMode.SINGLE;
        } else if (this.mode == BucketMode.SINGLE) {
            this.list = new ArrayList<>();
            this.list.add(single);
            this.list.add(pair);
            this.single = null;
            this.mode = BucketMode.LIST;
        } else if (this.mode == BucketMode.LIST) {
            if (this.list.size() < LIST_LIMIT) {
                this.list.add(pair);
            } else {
                this.node = new Node<>(pair.getKey(), pair.getValue());
                for (int i=0;i<this.list.size();i++) {
                    final Pair<V> cur = this.list.get(i);
                    this.node.insert(cur.getKey(), cur.getValue());
                }
                this.mode = BucketMode.NODE;
                this.list = null;
            }
        } else {
            this.node.insert(pair.getKey(), pair.getValue());
        }
    }

    private Optional<Pair<V>> lookFor(long key) {
        if (this.mode == BucketMode.SINGLE) {
            if (this.single.getKey().equals(key)) {
                return Optional.of(this.single);
            } else {
                return Optional.empty();
            }
        } else if (this.mode == BucketMode.LIST) {
            for (int i=0;i<this.list.size();i++) {
                final Pair<V> pair = this.list.get(i);
                if (pair.getKey().equals(key)) {
                    return Optional.of(pair);
                }
            }
        } else {
            final Optional<Node<V>> node = this.node.get(key);
            if (node.isPresent()) {
                return Optional.of(new Pair(key, node.get().getValue()));
            }
        }
        return Optional.empty();
    }

    public Pair<V> remove(long key) {
        V value;
        Pair<V> result;
        if (this.mode == BucketMode.SINGLE) {
            value = this.single.getValue();
            this.single = null;
            result = new Pair(key, value);
            return result;
        } else if (this.mode == BucketMode.LIST) {
            Pair<V> pair = null;
            int index = 0;
            for (int i=0;i<this.list.size();i++) {
                pair = this.list.get(i);
                if (pair.getKey().equals(key)) {
                    index = i;
                    break;
                }
            }
            if (pair != null) {
                this.list.remove(index);
                return pair;
            }
        } else if (this.mode == BucketMode.NODE) {
            final Optional<Node<V>> node = this.node.get(key);
            final Pair<V> pair = new Pair(node.get().getKey(), node.get().getValue());
            this.node.remove(key);
            return pair;
        }
        return null;
    }

    public boolean containsValue(V value) {
        if (this.mode == BucketMode.SINGLE) {
            if (this.single.getValue().equals(value)) {
                return true;
            }
        } else if (this.mode == BucketMode.LIST) {
            for (int i=0;i<this.list.size();i++) {
                final Pair<V> pair = this.list.get(i);
                if (pair.getValue().equals(value)) {
                    return true;
                }
            }
        } else {
            if (this.node.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    public Optional<Pair<V>> get(long key) {
        return lookFor(key);
    }

    public List<Long> getKeys() {
        if (this.mode == BucketMode.SINGLE) {
            return Arrays.asList(this.single.getKey());
        } else if (this.mode == BucketMode.LIST) {
            return this.list.stream().map(item -> item.getKey()).collect(Collectors.toList());
        } else {
            return this.node.getAllKeys();
        }
    }

    public List<V> getValues() {
        if (this.mode == BucketMode.SINGLE) {
            return Arrays.asList(this.single.getValue());
        } else if (this.mode == BucketMode.LIST) {
            return this.list.stream().map(item -> item.getValue()).collect(Collectors.toList());
        } else {
            return this.node.getAllValues();
        }
    }

    public boolean isEmpty() {
        return (this.single == null && this.list.isEmpty() && this.node == null);
    }

    public BucketMode getMode() {
        return this.mode;
    }

    public int size() {
        if (this.mode == BucketMode.SINGLE) {
            return 1;
        }
        if (this.mode == BucketMode.LIST) {
            return this.list.size();
        }
        if (this.mode == BucketMode.NODE) {
            return this.node.size();
        }
        return 0;
    }
}