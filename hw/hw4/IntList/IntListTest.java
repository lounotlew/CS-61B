import static org.junit.Assert.*;
import org.junit.Test;

public class IntListTest {

    /* Sample test that verifies correctness of the IntList.list static
       method. */

    @Test
    public void testList() {
        IntList one = new IntList(1, null);
        IntList twoOne = new IntList(2, one);
        IntList threeTwoOne = new IntList(3, twoOne);

        IntList x = IntList.list(3, 2, 1);
        assertEquals(threeTwoOne, x);
    }

    /** Do not use the new keyword in your tests. You can create
        lists using the handy IntList.list method.

        Make sure to include test cases involving lists of various sizes
        on both sides of the operation. That includes the empty list, which
        can be instantiated, for example, with
        IntList empty = IntList.list().

        Keep in mind that dcatenate(A, B) is NOT required to leave A untouched.
        Anything can happen to A. */

    @Test
    public void testDcatenate() {
        IntList empty = IntList.list();
        IntList oneTwo = IntList.list(1, 2);
        assertEquals(oneTwo, IntList.dcatenate(empty, oneTwo));

        oneTwo = IntList.list(1, 2);
        assertEquals(oneTwo, IntList.dcatenate(oneTwo, empty));

        IntList three = IntList.list(3);
        assertEquals(IntList.list(3, 1, 2), IntList.dcatenate(three, oneTwo));

    }

    /** some kind of information */

    @Test
    public void testSubtail() {
        IntList x012345 = IntList.list(0, 1, 2, 3, 4, 5);
        IntList copyOfx012345 = IntList.list(0, 1, 2, 3, 4, 5);

        /* Last 6 items should be the same as the entire list */
        assertEquals(copyOfx012345, IntList.subTail(x012345, 0));

        /* List should not change after call to subTail */
        assertEquals(x012345, copyOfx012345);

        /* Last 2 items should yield (5, 6) */
        IntList x45 = IntList.list(4, 5);
        assertEquals(x45, IntList.subTail(x012345, 4));
        assertEquals(x012345, copyOfx012345);

        /* Asking for 0 items should give null */
        assertEquals(IntList.subTail(x012345, 7), null);
    }


    @Test
    public void testSublist() {
        IntList x012345 = IntList.list(0, 1, 2, 3, 4, 5);
        IntList copyOfx012345 = IntList.list(0, 1, 2, 3, 4, 5);

        assertEquals(copyOfx012345, IntList.sublist(x012345, 0, 6));
        assertEquals(x012345, copyOfx012345);

        IntList x45 = IntList.list(4, 5);
        assertEquals(x45, IntList.sublist(x012345, 4, 2));
        assertEquals(x012345, copyOfx012345);
        assertEquals(IntList.sublist(x012345, 2, 0), null);
    }

    @Test
    public void testDsublist() {
        IntList x012345 = IntList.list(0, 1, 2, 3, 4, 5);
        IntList copyOfx012345 = IntList.list(0, 1, 2, 3, 4, 5);

        assertEquals(copyOfx012345, IntList.dsublist(x012345, 0, 6));

        /* reinitialize x012345 since it might have changed after testDSublist */
        x012345 = IntList.list(0, 1, 2, 3, 4, 5);
        IntList x45 = IntList.list(4, 5);
        assertEquals(x45, IntList.dsublist(x012345, 4, 2));

        x012345 = IntList.list(0, 1, 2, 3, 4, 5);
        assertEquals(IntList.dsublist(x012345, 2, 0), null);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(IntListTest.class));
    }
}
