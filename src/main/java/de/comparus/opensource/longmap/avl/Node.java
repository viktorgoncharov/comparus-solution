package de.comparus.opensource.longmap.avl;

import java.util.Optional;

public class Node<V> {
    private Node<V> left;
    private Node<V> right;
    private Long key;
    private V value;
    private byte height;

    public Node(Long key, V value) {
        this.key = key;
        this.value = value;
    }

    public Optional<V> get(Long key) {
        if (this.key.equals(key)) {
            return Optional.of(this.getValue());
        } else if (this.key < key) {
            return this.right.get(key);
        } else {
            return this.left.get(key);
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
        return balance();
    }

    public Node<V> rotateRight() {
        final Node<V> q = this.left;
        this.left = q.right;
        q.right = this;
        q.refreshHeight();
        this.refreshHeight();
        return q;
    }

    public Node<V> rotateLeft() {
        final Node<V> p = this.right;
        this.right = p.left;
        p.left = this;
        p.refreshHeight();
        this.refreshHeight();
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
}