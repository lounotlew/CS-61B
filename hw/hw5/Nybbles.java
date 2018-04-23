/** Represents an array of integers each in the range -8..7.
 *  Such integers may be represented in 4 bits (called nybbles).
 *  @author Woo Sik (Lewis) Kim
 */
public class Nybbles {

    /** Maximum positive value of a Nybble. */
    public static final int MAX_VALUE = 7;

    /** Return an array of size N. */
    public Nybbles(int N) {
        // DON'T CHANGE THIS.
        _data = new int[(N + 7) / 8];
        _n = N;
    }

    /** Return the size of THIS. */
    public int size() {
        return _n;
    }

    /** Return the Kth integer in THIS array, numbering from 0.
     *  Assumes 0 <= K < N. */
    public int get(int k) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else {
            if (_data.length == 0)
                return 0;

            int arrayIndex = k / 8;
            int kthNybble = k - 32*(_data.length - 1);
            int numberOfRightShifts = 4*(kthNybble - 1);
            int num = _data[arrayIndex];

            if (k == 0)
                return num & 15;

            int shiftedNum = num >> numberOfRightShifts;

            return shiftedNum & 15;

        }
    }

    /** Set the Kth integer in THIS array to VAL.  Assumes
     *  0 <= K < N and -8 <= VAL < 8. */
    public void set(int k, int val) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else if (val < (-MAX_VALUE - 1) || val > MAX_VALUE) {
            throw new IllegalArgumentException();
        } else {
            int arrayIndex = k / 8;
            int kthNybble = k - 32*(_data.length - 1);
            int numberOfLeftShifts = 4*(kthNybble - 1);
            int num = _data[arrayIndex];

            int kthInteger = get(k);
            int operator = (15 | val);

            if (k == 0)
                num = num ^ operator;

            num = num ^ (operator << numberOfLeftShifts);

        }
    }
    

    // DON'T CHANGE OR ADD TO THESE.
    /** Size of current array (in nybbles). */
    private int _n;
    /** The array data, packed 8 nybbles to an int. */
    private int[] _data;
}
