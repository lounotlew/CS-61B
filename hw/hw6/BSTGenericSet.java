/** A Class named BSTGenericSet that implements the GenericSet interface
  *  using a BST as its core data structure. 
  *  @author Woo Sik (Lewis) Kim
  */
public class BSTGenericSet<T extends Comparable<T>> implements GenericSet<T> {

    /** Node Class for storing data. */
    private class Node {

        /** Create a Node with its value as x. */
        public Node(T x) {
            _value = x;
        }

        /** Return my value. */
        public T value() {
            return _value;
        }

        /** Return my left Node. */
        public Node left() {
            return _left;
        }

        /** Return my right Node. */
        public Node right() {
            return _right;
        }

        /** Value of this Node. */
        private T _value;
        /** My left Node, initially empty. */
        private Node _left;
        /** My right Node, initially empty. */
        private Node _right;

    }

    /** Create an empty BSTGenericSet. */
    public BSTGenericSet() {
        _root = null;
    }

    /** Create a BSTGenericSet with its root as X. */
    public BSTGenericSet(T x) {
        _root = new Node(x);
    }

    /** Overload contains with X. */
    public boolean contains(T x) {
        return contains(x, _root);
    }

    /** Overload put with X. */
    public void put(T x) {
        put(x, _root);
    }

    /** Returns true if this set contains X. If T is null or
     *  the set does not contain X, return false. */
    boolean contains(T x, Node n) {
        if (n.value() == null)
            return false;
        else if (x == null)
            return false;
        int compared = x.compareTo(n.value());
        if (compared < 0)
            return contains(x, n.left());
        else if (compared > 0)
            return contains(x, n.right());
        return true;
    }

    /** Add X to the BSTStringSet. If the set already
     *  contains X, or if X is null, do nothing. */
    void put(T x, Node n) {
        if (n == null) {
            n = new Node(x);
            return;
        } else if (x == null)
            return;
        int compared = x.compareTo(n.value());
        if (compared < 0)
            put(x, n.left());
        else if (compared > 0)
            put(x, n.right());
        else if (compared == 0)
            return;
    }

    /** The root of this BSTGenericSet. */
    private Node _root;

}
