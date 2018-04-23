/** A Class named BSTStringSet that implements the StringSet interface
  *  using a BST as its core data structure. 
  *  @author Woo Sik (Lewis) Kim
  */
public class BSTStringSet implements StringSet {

    /** Node Class for storing data. */
    private class Node {

        /** Create a Node with its value as S. */
        public Node(String s) {
            _value = s;
        }

        /** Return my value. */
        public String value() {
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

        /** String value of this Node. */
        private String _value;
        /** My left Node, initially empty. */
        private Node _left;
        /** My right Node, initially empty. */
        private Node _right;

    }

    /** Create an empty BSTStringSet. */
    public BSTStringSet() {
        _root = null;
    }

    /** Create a BSTStringSet with its root as S. */
    public BSTStringSet(String s) {
        _root = new Node(s);
    }

    /** Overload contains with S. */
    public boolean contains(String s) {
        return contains(s, _root);
    }

    /** Overload put with S. */
    public void put(String s) {
        put(s, _root);
    }

    /** Returns true if this set contains S. If S is null or
     *  the set does not contain S, return false. */
    boolean contains(String s, Node n) {
        if (n.value() == null)
            return false;
        else if (s == null)
            return false;
        int compared = s.compareTo(n.value());
        if (compared < 0)
            return contains(s, n.left());
        else if (compared > 0)
            return contains(s, n.right());
        return true;
    }

    /** Add String S to the BSTStringSet. If the set already
     *  contains S, or if S is null, do nothing. */
    void put(String s, Node n) {
        if (n == null) {
            n = new Node(s);
            return;
        } else if (s == null)
            return;
        int compared = s.compareTo(n.value());
        if (compared < 0)
            put(s, n.left());
        else if (compared > 0)
            put(s, n.right());
        else if (compared == 0)
            return;
    }

    /** The root of this BSTStringSet. */
    private Node _root;

}
