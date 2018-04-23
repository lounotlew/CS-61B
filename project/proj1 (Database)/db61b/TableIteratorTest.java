package db61b;

import static org.junit.Assert.*;
import org.junit.Test;

/** Tests of the TableIterator class.
 *  @author Woo Sik (Lewis) Kim
 */
public class TableIteratorTest {

    @Test
    public void testTableIterator() {
        String[] colTitles = {"SID", "Name", "Year"};
        Table table = new Table("Student Info", colTitles);

        TableIterator iter = table.tableIterator();

        assertEquals(null, iter.next());

        String[] contents1 = {"34455553", "Jack", "1990"};
        Row row1 = new Row(contents1);
        table.add(row1);
        String[] contents2 = {"23311112", "John", "1992"};
        Row row2 = new Row(contents2);
        table.add(row2);

        iter.reset();

        assertEquals(row2, iter.next());
        iter.next();
        iter.next();
        assertTrue(iter != null);
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(TableIteratorTest.class));
    }

}
