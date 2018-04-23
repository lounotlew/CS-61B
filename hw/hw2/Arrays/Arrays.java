/** Solutions to HW #2, Problem #2.
 *  @author P. N. Hilfinger
 */
class Arrays {
    /* 2a. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int[] r = new int[A.length + B.length];
        System.arraycopy(A, 0, r, 0, A.length);
        System.arraycopy(B, 0, r, A.length, B.length);
        return r;
    }

    /* Here's an alternative solution to a, using loops: */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate2(int[] A, int[] B) {
        int[] r = new int[A.length + B.length];
        int i;
        for (i = 0; i < A.length; i += 1) {
            r[i] = A[i];
        }
        for (int j = 0; j < B.length; j += 1, i += 1) {
            r[i] = B[j];
        }
        return r;
    }

    /* 2b. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        if (len == 0) {
            return A;
        }
        int[] r = new int[A.length - len];
        System.arraycopy(A, 0, r, 0, start);
        System.arraycopy(A, start + len, r, start, A.length - start - len);
        return r;
    }

    /* 4. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        return naturalRuns(A, 0, 0);
    }

    /** Returns an array, r, whose first LEN elements are 0 and whose
     *  remaining elements are the natural runs of A[NEXT .. A.length-1]. */
    private static int[][] naturalRuns(int[] A, int next, int len) {
        if (A.length <= next) {
            return new int[len][];
        }
        int k = runLength(A, next);
        int[][] result = naturalRuns(A, next + k, len + 1);
        result[len] = Utils.subarray(A, next, k);
        return result;
    }

    /** Assuming 0 <= K < A.length, returns the length of the longest
     *  strictly increasing sequence of elements of A starting at K. */
    private static int runLength(int[] A, int k) {
        int i;
        i = k + 1;
        while (i < A.length && A[i] > A[i - 1]) {
            i += 1;
        }
        return i - k;
    }

}
