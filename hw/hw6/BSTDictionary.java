/** A Class named BSTDictionary that implements the SimpleDictionary interface
  *  using a BST as its core data structure. 
  *  @author Woo Sik (Lewis) Kim
  */
public class BSTDictionary implements SimpleDictionary {

	/** Node Class for storing data. */
	private class Node {

        /** Create a Node containing WORD and DEF. */
        public Node(String word, String def) {
            _word = word;
            _def = def;
        }

        /** Return my value. */
        public String word() {
            return _word;
        }

        /** Return my definition. */
        public String def() {
        	return _def;
        }

        /** Return my left Node. */
        public Node left() {
            return _left;
        }

        /** Return my right Node. */
        public Node right() {
            return _right;
        }

        /** The word and its definition contained in this Node. */
        private String _word, _def;
        /** My left Node, initially empty. */
        private Node _left;
        /** My right Node, initially empty. */
        private Node _right;

    }

    /** Create an empty BSTDictionary. */
    public BSTDictionary() {
        _root = null;
    }

    /** Create a BSTDictionary with its root as a Node containing
     *  WORD and DEF. */
    public BSTDictionary(String word, String def) {
        _root = new Node(word, def);
    }

    /** Overload contains with WORD and DEFINITION. */
    public boolean contains(String word) {
        return contains(word, _root);
    }

    /** Overload put with WORD and DEFINITION. */
    public void put(String word, String definition) {
        put(word, definition, _root);
    }

    /** Overload get with WORD. */
    public String get(String word) {
    	return get(word, _root);
    }

    /** Returns true if this set contains WORD. If WORD is null or
     *  the set does not contain WORD, return false. */
    boolean contains(String word, Node n) {
        if (n == null)
            return false;
        else if (word == null)
            return false;
        int compared = word.compareTo(n.word());
        if (compared < 0)
            return contains(word, n.left());
        else if (compared > 0)
            return contains(word, n.right());
        return true;
    }

    /** Add WORD and DEF to the BSTDictionary. If the set already
     *  contains WORD, or if WORD is null, do nothing. */
    void put(String word, String def, Node n) {
        if (n == null) {
            n = new Node(word, def);
            return;
        } else if (word == null)
            return;
        int compared = word.compareTo(n.word());
        if (compared < 0)
            put(word, def, n.left());
        else if (compared > 0)
            put(word, def, n.right());
        return;
    }

    /** Return the definition of WORD, or null if WORD is not
     *  in this BSTDictionary. */
    String get(String word, Node n) {
    	if (n == null)
    		return null;
    	int compared = word.compareTo(n.word());
    	if (compared < 0)
    		get(word, n.left());
    	else if (compared > 0)
    		get(word, n.right());
    	return n.def();
    }

    /** The root of this BSTDictionary. */
    private Node _root;

}