import java.util.Arrays;

/** A ECHashStringSet that extends the BSTStringSet.
 *  @author Woo Sik (Lewis) Kim */
class ECHashStringSet extends BSTStringSet {

    /** Create an ECHashStringSet with an initial size of 0 and
     *  its data as a String array of size 5. */
    public ECHashStringSet() {
    	_size = 0;
    	_data = new String[5];
    }

    /** Puts S in this. If this already contains S, does nothing. */
    @Override
    public void put(String s) {
    	if (s == null) {
    		return;
    	} else if (contains(s)) {
            return;
        } else {
    		if (((double)_size) / ((double)_data.length) > max) {
    			resize();
    		}
    		int k = removeTopBit(s.hashCode());

    		if (_data[k] == null)
    			_data[k] = null;
    		else {
    			_data[k] = s;
    			_size += 1;
    		}
    	}
    }

    /** Return true if S is in this. Else, return false. */
    @Override
    public boolean contains(String s) {
    	if (s == null) {
    		return false;
    	} else {
    		int k = removeTopBit(s.hashCode());

    		if (_data[k] == null)
    			return false;

    		return true;
    	}
    }

    /** Resizes this's data structure if its load factor is greater
     *  than 5. */
	void resize() {
		String[] temp = _data;
		_data = new String[2*temp.length];
		_size = 0;

		for (String s : temp)
			put(s);
	}

    /** Removes the top bit of N to convert it into an unsigned integer. */
	int removeTopBit(int n) {
		int lastBit = n & 1;
		int unsignedN = (n >>> 1) | lastBit;

		return unsignedN;
	}


	private double min = 0.2;
	private double max = 5;
	private String[] _data;
	private int _size;

}
