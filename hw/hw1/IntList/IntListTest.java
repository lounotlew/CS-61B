import static org.junit.Assert.*;
import org.junit.Test;

public class IntListTest {

    /** Example test that verifies correctness of the IntList.list static
     *  method. The main point of this is to convince you that
     *  assertEquals knows how to handle IntLists just fine.
     */

    @Test
    public void testList() {
        IntList one = new IntList(1, null);
        IntList twoOne = new IntList(2, one);
        IntList threeTwoOne = new IntList(3, twoOne);

        IntList x = IntList.list(3, 2, 1);
        assertEquals(threeTwoOne, x);
    }

    /** Do not use the new keyword in your tests. You can create
     *  lists using the handy IntList.list method.
     *
     *  Make sure to include test cases involving lists of various sizes
     *  on both sides of the operation. That includes the empty list, which
     *  can be instantiated, for example, with
     *  IntList empty = IntList.list().
     *
     *  Keep in mind that dcatenate(A, B) is NOT required to leave A untouched.
     *  Anything can happen to A.
     */

    @Test
    public void testDcatenate() {
        IntList a1 = IntList.list(1);
        IntList b1 = IntList.list(2);
        IntList empty = null;
        IntList a2 = IntList.list(1, 2, 3);
        IntList b2 = IntList.list(4, 5, 6);
        IntList combined1 = IntList.dcatenate(a1, b1);
        IntList emptyCombined = IntList.dcatenate(empty, b2);
        IntList combined2 = IntList.dcatenate(a2, b2);

        IntList x1 = IntList.list(1, 2);
        IntList xEmpty = IntList.list(4, 5, 6);
        IntList x2 = IntList.list(1, 2, 3, 4, 5, 6);

        assertEquals(combined1, x1);
        assertEquals(emptyCombined, xEmpty);
        assertEquals(combined2, x2);
    }

    /** Tests that subtail works properly. Again, don't use new.
     *
     *  Make sure to test that subtail does not modify the list.
     */

    @Test
    public void testSubtail() {
        IntList A1 = IntList.list(1, 2, 3, 4, 5);
        IntList A2 = IntList.subTail(A1, 0);
        IntList B1 = IntList.list(3, 4, 5);
        IntList B2 = IntList.subTail(A1, 2);

        assertEquals(A1, A2);
        assertEquals(B1, B2);
    }

    /** Tests that sublist works properly. Again, don't use new.
     *
     *  Make sure to test that sublist does not modify the list.
     */

    @Test
    public void testSublist() {
        IntList OrgList = IntList.list(1, 2, 3, 4, 5);
        IntList Start1Len2 = IntList.list(2, 3);
        IntList Start0Len0 = null;
        IntList Start4Len1 = IntList.list(5);

        IntList a1 = IntList.sublist(OrgList, 1, 2);
        IntList a2 = IntList.sublist(OrgList, 0, 0);
        IntList a3 = IntList.sublist(OrgList, 4, 1);

        assertEquals(a1, Start1Len2);
        assertEquals(a2, Start0Len0);
        assertEquals(a3, Start4Len1);
    }

    /** Tests that dSublist works properly. Again, don't use new.
     *
     *  As with testDcatenate, it is not safe to assume that list passed
     *  to dSublist is the same after any call to dSublist
     */

    @Test
    public void testDsublist() {
        IntList OrgList = IntList.list(1, 2, 3, 4, 5);
        IntList Start1Len2 = IntList.list(2, 3);
        IntList Start0Len0 = null;
        IntList Start4Len1 = IntList.list(5);

        IntList a1 = IntList.dsublist(OrgList, 1, 2);
        IntList a2 = IntList.dsublist(OrgList, 0, 0);
        IntList a3 = IntList.dsublist(OrgList, 4, 1);

        assertEquals(a1, Start1Len2);
        assertEquals(a2, Start0Len0);
        assertEquals(a3, Start4Len1);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(IntListTest.class));
    }
}
