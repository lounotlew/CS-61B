import java.util.Iterator;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Josh Hug and Woo Sik (Lewis) Kim
 */
public class BSTStringSet implements SortedStringSet {
    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        public Node(String sp) {
            s = sp;
        }

        /** Return my S. */
        public String data() {
            return s;
        }
    }

    /** Root node of the tree. */
    private Node root;

    /** Creates a new empty StringSet. */
    public BSTStringSet() {
        root = null;
    }

    /** Returns true if the String S is in the set. */
    public boolean contains(String s) {
        return contains(s, root);
    }

    /** Returns true if String S is in the subset rooted in P. */
    private boolean contains(String s, Node p) {
        if (p == null) {
            return false;
        }

        int cmp = s.compareTo(p.s);
        if (cmp < 0) {
            return contains(s, p.left);
        }
        if (cmp > 0) {
            return contains(s, p.right);
        }

        return true;
    }

    /** Inserts the string S if it is not already present.
     *  If it is already present, does nothing.
     */
    public void put(String s) {
        root = put(s, root);
    }

    /** Helper method for put. Returns a BST rooted in P,
      *  but with S added to this BST.
      */
    private Node put(String s, Node p) {
        if (p == null) {
            return new Node(s);
        }

        int cmp = s.compareTo(p.s);

        if (cmp < 0) {
            p.left = put(s, p.left);
        }
        if (cmp > 0) {
            p.right = put(s, p.right);
        }

        return p;
    }

    @Override
    public Iterator<String> iterator(String L, String U) {
        return iterator(L, U, root);
    }

    Iterator<String> iterator(String L, String U, Node n) {
        if (n.data() == null)
            return _data.iterator();
        else {
            if (U.compareTo(n.data()) < 0) {
                if (L.compareTo(n.data()) == 0) {
                    _data.add(n.data());
                } else if (L.compareTo(n.data()) > 0) {
                    iterator(L, U, n.left);
                } else if (L.compareTo(n.data()) < 0) {
                    iterator(L, U, n.right);
                }
            } else if (U.compareTo(n.data()) > 0) {
                return _data.iterator();
            } else if (U.compareTo(n.data()) == 0) {
                _data.add(n.data());
            }
        }
        return _data.iterator();
    }

    /** Prints the Set in increasing order. */
    public void printSet() {
        printInOrder(root);
        System.out.println();
    }

    /** Helper method that prints BST rooted in P. */
    private void printInOrder(Node p) {
        if (p == null) {
            return;
        }

        printInOrder(p.left);
        System.out.print(p.s + " ");
        printInOrder(p.right);
    }

    /** My data for iterator. */
    private Stack<String> _data;
}
