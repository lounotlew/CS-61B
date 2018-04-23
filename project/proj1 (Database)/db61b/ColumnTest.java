package db61b;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

/** Tests of the Column class.
 *  @author Woo Sik (Lewis) Kim
 */
public class ColumnTest {

    @Test
    public void testColumn() {
        String[] titles = {"name", "grade", "year"};
        Table table = new Table("student grades", titles);

        Column col1 = new Column(table, "name");
        Column col2 = new Column(table, "grade");
        Column col3 = new Column(table, "year");

        assertEquals(titles[0], col1.name());
        assertEquals(titles[1], col2.name());
        assertEquals(titles[2], col3.name());

        String[] contents = {"Jack", "A", "1990"};
        Row row1 = new Row(contents);
        table.add(row1);

        String[] contents2 = {"John", "B", "1992"};
        Row row2 = new Row(contents2);
        table.add(row2);

        List<TableIterator> iters = new ArrayList<TableIterator>();
        iters.add(table.tableIterator());
        col1.resolve(iters);
        col2.resolve(iters);
        col3.resolve(iters);

        assertEquals("Jack", col1.value());
        assertEquals("A", col2.value());
        assertEquals("1990", col3.value());
    }

    @Test
    public void testSpecifiedName() {
        String[] columnTitles = {"name", "lastname", "ID"};
        Table table = new Table("students", columnTitles);

        Column col = new Column(table, "name");

        assertEquals(0, col.name().compareTo("name"));
        assertEquals(null, col.specifiedName());

        col.setSpecifiedName("new name");

        assertEquals(0, col.specifiedName().compareTo("new name"));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ColumnTest.class));
    }

}
