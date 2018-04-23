/** Solution to HW #2, Problem #1.
 *  @author P. N. Hilfinger
 */
class Lists {
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10),
     *  then result is the three-item list ((1, 3, 7), (5), (4, 6, 9, 10)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntList2 naturalRuns(IntList L) {
        if (L == null) {
            return null;
        } else {
            IntList e = endOfRun(L);
            IntList2 rest = naturalRuns(e.tail);
            e.tail = null;
            return new IntList2(L, rest);
        }
    }

    /** Assuming L is not null, returns the last element of L in which
     *  the value of L.head increases from the previous element (the
     *  end of the list if L is entirely in strictly ascending order).  */
    private static IntList endOfRun(IntList L) {
        while (L.tail != null && L.tail.head > L.head) {
            L = L.tail;
        }
        return L;
    }

}
