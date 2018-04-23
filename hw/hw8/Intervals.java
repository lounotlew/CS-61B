import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/** HW #8, Problem 3.
 *  @author Woo Sik (Lewis) Kim
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {

        int length = 0;
        int start = Integer.MIN_VALUE;
        int end = Integer.MIN_VALUE;

        for (int i = 0; i < intervals.size(); i += 1) {
            if (intervals.get(i)[0] > end) {
                length += end - start;
                start = intervals.get(i)[0];
                end = intervals.get(i)[0];
            }
        }

        length += end - start;
        return length;
    }

    /** Performs a basic functionality test on the stabbingCount method. */
    @Test
    public void basicTest() {
        int[][] intervals = {
            {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
        };
        assertEquals(23, coveredLength(Arrays.asList(intervals)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
