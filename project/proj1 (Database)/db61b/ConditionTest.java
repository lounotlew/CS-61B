package db61b;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

/** Tests of the Condition class.
 *  @author Woo Sik (Lewis) Kim
 */
public class ConditionTest {

    @Test
    public void testCondition() {
        String[] titles = {"SID", "Name", "Grade", "Year"};
        String[] contents1 = {"1", "Jack", "A", "1990"};
        String[] contents2 = {"2", "John", "B", "1992"};
        String[] contents3 = {"3", "Sam", "C", "1993"};

        Row row1 = new Row(contents1);
        Row row2 = new Row(contents2);
        Row row3 = new Row(contents3);

        Table table1 = new Table("Grades", titles);
        table1.add(row1);
        table1.add(row2);
        table1.add(row3);

        Column col1 = new Column(table1, "SID");
        Column col2 = new Column(table1, "Name");
        Column col3 = new Column(table1, "Grade");
        Column col4 = new Column(table1, "Year");

        TableIterator iter = table1.tableIterator();
        List<TableIterator> iters = new ArrayList<TableIterator>();
        iters.add(iter);
        col1.resolve(iters);
        col2.resolve(iters);
        col3.resolve(iters);
        col4.resolve(iters);

        Condition greaterThan = new Condition(col1, ">", col2);
        Condition lessThan = new Condition(col1, "<", col2);
        Condition gtOrEq = new Condition(col1, ">=", col2);
        Condition ltOrEq = new Condition(col1, "<=", col2);
        Condition equalTo = new Condition(col1, "=", col2);
        Condition notEqualTo = new Condition(col1, "!=", col2);

        assertFalse(greaterThan.test());
        assertTrue(lessThan.test());
        assertFalse(gtOrEq.test());
        assertTrue(ltOrEq.test());
        assertFalse(equalTo.test());
        assertTrue(notEqualTo.test());
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ConditionTest.class));
    }

}
