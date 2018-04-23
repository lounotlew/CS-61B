package loa;

import ucb.junit.textui;
import org.junit.Test;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the loa package.
 *  @author Woo Sik (Lewis) Kim
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class, BoardTest.class, MoveTest.class);
    }

    /** A filler blank test. Unit tests are provided in individual files:
     *  BoardTest.java and MoveTest.java. */
    @Test
    public void filler() {

    }

}
