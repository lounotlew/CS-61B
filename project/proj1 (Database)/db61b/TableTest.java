package db61b;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

/** Tests of the Table class.
 *  @author Woo Sik (Lewis) Kim
 */
public class TableTest {

    @Test
    public void testTable() {
        String[] titles = {"1", "2", "3", "4"};
        String[] data = {"a", "b", "c", "d"};
        Row row1 = new Row(data);

        String[] data2 = {"val1", "val2", "val3", "val4"};
        Row row2 = new Row(data2);
        Row row3 = new Row(data2);

        ArrayList<Row> rows = new ArrayList<Row>();
        Table table = new Table("test", titles);

        assertEquals("test", table.name());
        assertTrue(table.add(row1));
        assertTrue(table.add(row2));
        assertFalse(table.add(row3));
        assertEquals(2, table.size());
        assertEquals(2, table.columnIndex("3"));
        assertEquals(4, table.numColumns());
    }

    @Test
    public void testReadWritePrint() {

        String printline = "a b c d";
        String[] arry = {"a", "b", "c", "d"};
        String converted = Table.arrayToString(arry, " ");

        assertEquals(0, printline.compareTo(converted));

        String dbLine = "a,b,c,d";
        String[] columnTitles = {"a", "b", "c", "d"};
        String converted2 = Table.arrayToString(columnTitles, ",");

        assertEquals(0, dbLine.compareTo(converted2));

        String printed = "  hello world";
        String[] arry2 = {"hello", "world"};
        String converted3 = "  " + Table.arrayToString(arry2, " ");

        assertEquals(0, printed.compareTo(converted3));
    }

    @Test
    public void testDistinctElem() {
        String[] distinct = {"a", "b", "c", "d", "e"};
        String[] notDistinct = {"a", "b", "b", "c", "a"};

        assertTrue(Table.distinctElements(distinct));
        assertFalse(Table.distinctElements(notDistinct));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(TableTest.class));
    }

}
