package db61b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import static db61b.Utils.*;

/** A single table in a database.
 *  @author Woo Sik (Lewis) Kim
 */
class Table implements Iterable<Row> {

    /** A new Table named NAME whose columns are give by COLUMNTITLES,
     *  which must be distinct (else exception thrown). */
    Table(String name, String[] columnTitles) {
        if (distinctElements(columnTitles)) {
            _name = name;
            _titles = columnTitles;
        } else {
            throw error("duplicate column name:" + " " + _duplicateName);
        }
    }

    /** A new Table named NAME whose column names are give by COLUMNTITLES. */
    Table(String name, List<String> columnTitles) {
        this(name, columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    int numColumns() {
        return _titles.length;
    }

    /** Returns my name. */
    String name() {
        return _name;
    }

    /** Returns a TableIterator over my rows in an unspecified order. */
    TableIterator tableIterator() {
        return new TableIterator(this);
    }

    /** Returns an iterator that returns my rows in an unspecfied order. */
    @Override
    public Iterator<Row> iterator() {
        return _rows.iterator();
    }

    /** Return the title of the Kth column.  Requires 0 <= K < columns(). */
    String title(int k) {
        return _titles[k];
    }

    /** Return the number of the column whose title is TITLE, or -1 if
     *  there isn't one. */
    int columnIndex(String title) {
        for (int i = 0; i < _titles.length; i += 1) {
            if (_titles[i].compareTo(title) == 0) {
                return i;
            }
        }
        return -1;
    }

    /** Return the number of Rows in this table. */
    int size() {
        return _rows.size();
    }

    /** Add ROW to THIS if no equal row already exists.  Return true if anything
     *  was added, false otherwise. */
    boolean add(Row row) {
        if (row.size() != numColumns()) {
            throw error("inserted rows have wrong length.");
        } else if (_rows.contains(row)) {
            return false;
        }
        _rows.add(row);
        return true;
    }

    /** Returns true if each element in ARRAY is distinct (no duplicate items
     *  exist within ARRAY. Else, returns false. */
    static boolean distinctElements(String[] array) {
        for (int i = 0; i < array.length; i += 1) {
            for (int j = i + 1; j < array.length; j += 1) {
                if (array[i].compareTo(array[j]) == 0) {
                    _duplicateName = array[i];
                    return false;
                }
            }
        }
        return true;
    }


    /** Convert a String array ARRAY into a string LINE, where each element
     *  of ARRAY is seperatd by SEP. Returns LINE. */
    static String arrayToString(String[] array, String sep) {
        String line = "";
        for (int i = 0; i < array.length; i += 1) {
            line = line + array[i];
            if (i + 1 < array.length) {
                line = line + sep;
            }
        }
        return line;
    }

    /** Read the contents of the file NAME.db, and return as a Table.
     *  Format errors in the .db file cause a DBException. */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        table = null;
        try {
            input = new BufferedReader(new FileReader(name + ".db"));
            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            table = new Table(name, columnNames);

            String nextLine = input.readLine();

            while (nextLine != null) {
                String[] columnValues = nextLine.split(",");
                if (columnValues.length != columnNames.length) {
                    throw new DBException();
                }
                table.add(new Row(columnValues));
                nextLine = input.readLine();
            }

        } catch (FileNotFoundException e) {
            throw error("could not find %s.db", name);
        } catch (IOException e) {
            throw error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    /* Ignore IOException */
                }
            }
        }
        return table;
    }

    /** Write the contents of TABLE into the file NAME.db. Any I/O errors
     *  cause a DBException. */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            output = new PrintStream(name + ".db");

            output.println(arrayToString(_titles, ","));

            for (Row row : _rows) {
                output.println(arrayToString(row.data(), ","));
            }

        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /** Print my contents on the standard output, separated by spaces
     *  and indented by two spaces. */
    void print() {
        for (Row row : _rows) {
            System.out.println("  " + arrayToString(row.data(), " "));
        }
    }

    /** My name. */
    private final String _name;
    /** My column titles. */
    private String[] _titles;
    /** A duplicate name in _titles if column titles are not distinct. */
    private static String _duplicateName;
    /** My rows. */
    private ArrayList<Row> _rows = new ArrayList<Row>();

}
