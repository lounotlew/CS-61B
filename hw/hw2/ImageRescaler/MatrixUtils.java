/** Provides a variety of utilities for operating on matrices.
 *  All methods assume that the double[][] arrays provided are rectangular.
 *
 *  @author Josh Hug and YOU
 */

public class MatrixUtils {
    /** enum for specifying vertical vs. horizontal orientation
     *  You can use this, for example, with Orientation x = VERTICAL
     *
     *  If you're using this from another class, you'll need to use the class
     *  name as well, e.g. MatrixUtils.Orientation x =
     *  MatrixUtils.Orientation.VERTICAL
     */

    static enum Orientation { VERTICAL, HORIZONTAL };

    /** Non-destructively accumulates a matrix in the vertical direction.
     *  Given a matrix M, returns a new matrix am[][] such that for
     *  each index r, c, am[r][c] is equal to the sum of m[r][c] +
     *  m[r-1][c] + ... + m[0][c].
     *
     *  Potentially useful methods: MatrixUtils.copy
     *
     *  You might also find it helpful to write a helper method that takes
     *  as input a matrix, a row index, a column index, and returns
     *  the value in the matrix if the row/column indices are valid, and
     *  Double.POSITIVE_INFINITY otherwise.
     */

    public static double[][] accumulateVertical(double[][] m) {
        m = copy(m);

        for (int r = 1; r < m.length; r++) {
            for (int c = 0; c < m[0].length; c++) {
                double best = Double.POSITIVE_INFINITY;
                for (int deltaC = -1; deltaC <= 1; deltaC += 1) {
                    if (get(m, r - 1, c + deltaC) < best) {
                        best = get(m, r - 1, c + deltaC);
                    }
                }
                m[r][c] += best;
            }
        }
        return m;
    }

    /** Non-destructively accumulates a matrix M along the specified
     *  ORIENTATION.
     *
     *  If the orientation is Orientation.VERTICAL, function is identical to
     *  accumulateVertical. If Orientation.HORIZONTAL, function is
     *  the same, but with roles of r and c reversed.
     *
     *  Do NOT copy and paste a bunch of code! Instead, you should write
     *  a helper function that creates a new matrix mT that contains all
     *  the information from m, but with the property that
     *  accumulateVertical(mT) returns the correct result.
     *
     *  accumulate should be very short (only a few lines). Most of the
     *  work should be done in creaing the helper function (and even
     *  that function should be pretty short and straightforward).
     *
     *  The important lesson here is that you should never have big
     *  copy and pastes of any code. Instead, find the right
     *  abstraction that lets you avoid this mess. You'll need to do this
     *  for project 1, but in a more complex way.
     */

    public static double[][] accumulate(double[][] m, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            m = transpose(m);
        }

        return transpose(accumulateVertical(m));
    }


    /** Returns transpose of M.
    */

    private static double[][] transpose(double[][] m) {
        int width = m[0].length;
        int height = m.length;

        double[][] mT = new double[width][height];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                mT[c][r] = m[r][c];
            }
        }
        return mT;
    }

    /** Gets entry of M, unless R or C are out of bounds, then
     *  we return infinity instead. */

    private static double get(double[][] m, int r, int c) {
        int width = m[0].length;
        int height = m.length;
        if ((c < 0) || (c >= width) || (r < 0) || (r >= height)) {
            return Double.POSITIVE_INFINITY;
        }
        return m[r][c];
    }

    /** returns index of smallest item in X. */

    private static int mindex(double[] x) {
        int mindex = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] < x[mindex]) {
                mindex = i;
            }
        }

        return mindex;
    }

    /** Returns index of smallest item in X between indices LO and HI
     *  inclusive.
     */

    private static int mindex(double[] x, int lo, int hi) {
        if (lo < 0) {
            lo = 0;
        }

        if (hi >= x.length) {
            hi = x.length - 1;
        }

        int mindex = lo;
        for (int i = lo; i <= hi; i++) {
            if (x[i] < x[mindex]) {
                mindex = i;
            }
        }

        return mindex;
    }

    /** Finds the vertical seam VERTSEAM of the given matrix M.
     *
     *  Potentially useful helper function: Something that takes
     *  an array, a lo index, and a hi index, and returns the
     *  index of the smallest thing in that array between those
     *  indices (inclusive).
     *
     *  Such a helper function will keep your code simple.
     *
     *  To keep the HW from getting too long, this method (and the
     *  next) is optional. however, completing it will allow you to
     *  see the resizing algorithm (that you wrote!) in action.
     */

    public static int[] findVerticalSeam(double[][] m) {

        int height = m.length;
        int width = m[0].length;

        int[] vertSeam = new int[height];
        int r = height - 1;
        vertSeam[r] = mindex(m[r]);

        for (r = height - 2; r >= 0; r--) {
            int minOK = vertSeam[r + 1] - 1;
            int maxOK = vertSeam[r + 1] + 1;

            int x = mindex(m[r], minOK, maxOK);
            vertSeam[r] = x;

        }
        return vertSeam;
    }

    /** Returns the SEAM of M with the given ORIENTATION.
     *  As with accumulate, this should be really short and use
     *  a helper method.
     */

    public static int[] findSeam(double[][] m, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            m = transpose(m);
        }

        int[] seam = findVerticalSeam(m);
        return seam;
    }

    /** does nothing. ARGS not used. use for whatever purposes you'd like */
    public static void main(String[] args) {
        /* sample calls to functions in this class are below

        ImageRescaler sc = new ImageRescaler("4x6.png");

        double[][] em = ImageRescaler.energyMatrix(sc);

        //double[][] m = sc.cumulativeEnergyMatrix(true);
        double[][] m = MatrixUtils.accumulateVertical(em);
        System.out.println(MatrixUtils.matrixToString(em));
        System.out.println();

        double[][] ms = MatrixUtils.accumulate(em, Orientation.HORIZONTAL);

        System.out.println(MatrixUtils.matrixToString(m));
        System.out.println();
        System.out.println(MatrixUtils.matrixToString(ms));


        int[] lep = MatrixUtils.findVerticalSeam(m);

        System.out.println(seamToString(m, lep, Orientation.VERTICAL));

        int[] leps = MatrixUtils.findSeam(ms, Orientation.HORIZONTAL);

        System.out.println(seamToString(ms, leps, Orientation.HORIZONTAL));
        */
    }


    /** Below follow some utility functions. Also see Utils.java. */

    /** Returns a string representaiton of the given matrix M.
     */

    public static String matrixToString(double[][] m) {
        int height = m.length;
        int width = m[0].length;
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                sb.append(String.format("%9.0f ", m[r][c]));
            }

            sb.append("\n");
        }
        return sb.toString();
    }

    /** Returns a copy of the matrix M. Note that it is probably a better idea
     *  to use System.arraycopy for general 2D arrays since
     */

    public static double[][] copy(double[][] m) {
        int height = m.length;
        int width = m[0].length;

        double[][] copy = new double[height][];
        for (int r = 0; r < height; r++) {
            copy[r] = new double[width];
            double[] thisRow = m[r];
            System.arraycopy(thisRow, 0, copy[r], 0, width);
        }
        return copy;
    }

    /** Checks to see if a given SEAM is valid given a particular
     *  image HEIGHT, WIDTH, and a seam ORIENTATION. Throws an illegal
     *  argument exception if invalid.
     */

    public static void validateSeam(int height, int width,
                           int[] seam, Orientation orientation) {

        if ((orientation == Orientation.VERTICAL)
             && (seam.length != height)) {
            throw new IllegalArgumentException("Bad vertical seam length.");
        }

        if ((orientation == Orientation.HORIZONTAL)
            && (seam.length != width)) {
            throw new IllegalArgumentException("Bad horizontal seam length.");
        }

        int maxValue;
        if (orientation == Orientation.VERTICAL) {
            maxValue = width - 1;
        } else {
            maxValue = height - 1;
        }


        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= maxValue) {
                String msg = "Seam value out of bounds.";
                throw new IllegalArgumentException(msg);
            }
        }

        for (int i = 0; i < seam.length - 1; i++) {
            int difference = Math.abs(seam[i] - seam[i + 1]);


            if (difference > 1) {
                String msg = "Seam not contiguous.";
                throw new IllegalArgumentException(msg);
            }
        }
    }


    /** Returns a string representation of  the matrix M annotated with the 
     *  provided SEAM along the ORIENTATION specified.
     */

    public static String seamToString(double[][] m, int[] seam,
                                     Orientation orientation) {

        StringBuilder sb = new StringBuilder();
        int height = m.length;
        int width = m[0].length;

        validateSeam(height, width, seam, orientation);

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                char lMarker = ' ';
                char rMarker = ' ';
                if (orientation == Orientation.VERTICAL) {
                    if (c == seam[r]) {
                        lMarker = '[';
                        rMarker = ']';
                    }
                } else {
                    if (r == seam[c]) {
                        lMarker = '[';
                        rMarker = ']';
                    }
                }
                sb.append(String.format("%c%6.0f%c ",
                          lMarker, m[r][c], rMarker));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}
