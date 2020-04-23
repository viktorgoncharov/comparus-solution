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

    /**
     *
     * @param pair
     * @return              true means a new item has been added, false - replaced only
     */
    public boolean addItem(Pair<V> pair) {
        boolean found = false;
        if (this.mode == null) {
            this.single = pair;
            this.mode = BucketMode.SINGLE;
            return true;
        } else if (this.mode == BucketMode.SINGLE) {
            if (this.single.getKey().equals(pair.getKey())) {
                this.single = pair;
                return false;
            } else {
                this.list = new ArrayList<>();
                this.list.add(single);
                this.list.add(pair);
                this.single = null;
                this.mode = BucketMode.LIST;
                return true;
            }
        } else if (this.mode == BucketMode.LIST) {
            for (int i=0;i<this.list.size();i++) {
                final Pair<V> p = this.list.get(i);
                if (p.getKey().equals(pair.getKey())) {
                    this.list.set(i, pair);
                    found = true;
                }
            }
            if (found) {
                return true;
            }
            if (this.list.size() < LIST_LIMIT) {
                this.list.add(pair);
                return true;
            } else {
                final Pair<V> first = this.list.get(0);
                this.node = new Node<>(first.getKey(), first.getValue());
                for (int i=1;i<this.list.size();i++) {
                    final Pair<V> cur = this.list.get(i);
                    this.node = this.node.insert(cur.getKey(), cur.getValue());
                }
                found = false;
                Optional<Node<V>> exist = this.node.get(pair.getKey());
                found = exist.isPresent();
                this.node = this.node.insert(pair.getKey(), pair.getValue());
                this.mode = BucketMode.NODE;
                this.list = null;
                return !found;
            }
        } else {
            found = false;
            Optional<Node<V>> exist = this.node.get(pair.getKey());
            found = exist.isPresent();
            this.node = this.node.insert(pair.getKey(), pair.getValue());
            return !found;
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
            this.node = this.node.remove(key);
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
        return (this.single == null && (this.list == null || this.list.isEmpty()) && this.node == null);
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

    public int countAll() {
        if (this.mode == BucketMode.SINGLE) {
            return 1;
        }
        if (this.mode == BucketMode.LIST) {
            return this.list.size();
        }
        if (this.mode == BucketMode.NODE) {
            return this.node.countAllNodes();
        }
        return 0;
    }

}