package de.comparus.opensource.longmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Node<V> {
    private Node<V> left;
    private Node<V> right;
    private Long key;
    private V value;
    private byte height;
    private int size = 1;

    public Node(Long key, V value) {
        this.key = key;
        this.value = value;
    }

    public Optional<Node<V>> get(Long key) {
        if (this.key.equals(key)) {
            return Optional.of(this);
        } else if (this.key < key) {
            if (this.right != null) {
                return this.right.get(key);
            } else {
                return Optional.empty();
            }
        } else {
            if (this.left != null) {
                return this.left.get(key);
            } else {
                return Optional.empty();
            }
        }
    }

    public Node<V> insert(Long key, V value) throws Exception {
        // Assumption: the key is not null for LongMap
        if (key == null) {
            throw new Exception("Key can't be null");
        }
        if (key < getKey()) {
            if (this.left != null) {
                this.left = this.left.insert(key, value);
            } else {
                this.left = new Node<>(key, value);
            }
        } else {
            if (this.right != null) {
                this.right = this.right.insert(key, value);
            } else {
                this.right = new Node<>(key, value);
            }
        }
        this.size++;
        return this.balance();
    }

    /**
     * Looking for a node with the minimal key
     *
     * @return
     */
    private Node<V> findMinKey() {
        if (this.left != null) {
            return this.left.findMinKey();
        } else {
            return this;
        }
    }

    private Node<V> removeMin() {
        if (this.left == null) {
            return this.right;
        }
        this.left = this.left.removeMin();
        return this.balance();
    }

    public Node<V> remove(Long key) {
        if (key < this.getKey()) {
            this.left = this.left.remove(key);
        } else if (key > this.getKey()) {
            this.right = this.right.remove(key);
        } else {
            Node<V> l = this.left;
            Node<V> r = this.right;
            if (r != null) {
                return l;
            }
            Node<V> min = r.findMinKey();
            min.right = r.removeMin();
            min.left = l;
            return min.balance();
        }
        this.size--;
        return balance();
    }

    public Node<V> rotateRight() {
        final Node<V> q = this.left;
        this.left = q.right;
        q.right = this;
        this.refreshHeight();
        q.refreshHeight();
        return q;
    }

    public Node<V> rotateLeft() {
        final Node<V> p = this.right;
        this.right = p.left;
        p.left = this;
        this.refreshHeight();
        p.refreshHeight();
        return p;
    }

    /**
     * Balancing the node
     *
     * @return                              balanced node
     */
    public Node<V> balance() {
        refreshHeight();
        final int balanceFactor = calcBalanceFactor();
        if ( balanceFactor == 2) {
            if (this.right.calcBalanceFactor() < 0) {
                this.right = this.right.rotateRight();
            }
            return rotateLeft();
        }
        if (balanceFactor == -2) {
            if (this.left.calcBalanceFactor() > 0) {
                this.left = this.left.rotateLeft();
            }
            return rotateRight();
        }
        return this;
    }

    public byte getHeight() {
        return this.height;
    }

    private int getLeftHeight() {
        if (this.left!=null) {
            return this.left.getHeight() + 1;
        } else {
            return 0;
        }
    }

    private int getRightHeight() {
        if (this.right!=null) {
            return this.right.getHeight() + 1;
        } else {
            return 0;
        }
    }

    public int calcBalanceFactor() {
        int leftH = getLeftHeight();
        int rightH = getRightHeight();
        return rightH - leftH;
    }

    public Node<V> refreshHeight() {
        final int hLeft = getLeftHeight();
        final int hRight = getRightHeight();
        if (hLeft > hRight) {
            this.height = (byte) (hLeft);
        } else {
            this.height = (byte) (hRight);
        }
        return this;
    }

    public Long getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public boolean containsValue(V value) {
        boolean result = false;
        if (this.value.equals(value)) {
            return true;
        }
        if (this.left != null) {
            result = this.left.containsValue(value);
            if (result) {
                return true;
            }
        }
        if (this.right != null) {
            result = this.right.containsValue(value);
            if (result) {
                return true;
            }
        }
        return false;
    }

    /**
     * Root -> left -> right
     *
     * @return
     */
    public List<Long> getAllKeys() {
        List<Long> result = new ArrayList<Long>(Arrays.asList(this.key));
        if (this.left != null) {
            result.addAll(this.left.getAllKeys());
        }
        if (this.right != null) {
            result.addAll(this.right.getAllKeys());
        }
        return result;
    }

    /**
     * Root -> left -> right
     *
     * @return
     */
    public List<V> getAllValues() {
        List<V> result = new ArrayList<V>(Arrays.asList(this.value));
        if (this.left != null) {
            result.addAll(this.left.getAllValues());
        }
        if (this.right != null) {
            result.addAll(this.right.getAllValues());
        }
        return result;
    }

    public int size() {
        return this.size;
    }
}