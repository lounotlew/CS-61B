/** Represents the function that adds a constant.
 *  @author Josh Hug, P. N. Hilfinger
 */

public class Adder implements IntUnaryFunction {
    /** A new Adder that adds N. */
    public Adder(int n) {
        _n = n;
    }

    @Override
    public int apply(int x) {
        return x + _n;
    }

    /** Constant addend. */
    private int _n;

}
