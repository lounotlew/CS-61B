/** Accumulates a sum of inputs to which it is applied.
 *  @author Josh Hug, P. N. Hilfinger
 */

public class Summer implements IntUnaryFunction {

    /** A Summer with initial accumulation of 0. */
    public Summer() {
        _s = 0;
    }

    /** Returns the accumulated sum. */
    public int getS() {
        return _s;
    }

    @Override
    public int apply(int x) {
        _s += x;
        return _s;
    }

    /** Cumulative sum. */
    private int _s;
}
