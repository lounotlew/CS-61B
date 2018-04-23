package db61b;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

/** Tests of the Row class.
 *  @author Woo Sik (Lewis) Kim
 */
public class RowTest {

    @Test
    public void testRow() {
        String[] data = {"a", "b", "c", "d"};
        Row row1 = new Row(data);

        String[] data2 = {"val1", "val2", "val3", "val4"};
        Row row2 = new Row(data2);
        Row row3 = new Row(data2);

        ArrayList<Row> rows = new ArrayList<Row>();
        rows.add(row1);
        rows.add(row2);

        assertEquals(data, row1.data());
        assertEquals(0, data2[1].compareTo(row2.get(1)));

        assertEquals(4, row1.size());
        assertFalse(row2.size() == 3);

        assertEquals("d", row1.get(3));
        assertFalse(data[0].equals(row1.get(1)));
        assertEquals(row2.get(2), row3.get(2));
        assertEquals(row2, row3);
        assertTrue(rows.contains(row2));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(RowTest.class));
    }

}
