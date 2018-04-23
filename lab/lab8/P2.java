public class P2 {

    public static void main(String... ignored) {

    }

    String rootData() {
    	return root.getData();
    }

    String leftData() {
    	return left.getData();
    }

    String rightData() {
    	return right.getData();
    }


    private class Node {

    	public Node(String s) {
    		_data = s;
    	}

    	String getData() {
    		return _data;
    	}

        private String _data;
    }

    private Node root;
    private Node left;
    private Node right;

}


