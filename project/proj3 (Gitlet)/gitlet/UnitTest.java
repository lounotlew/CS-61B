package gitlet;

import java.io.File;
import ucb.junit.textui;
import org.junit.Test;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the gitlet package.
 *  @author Woo Sik (Lewis) Kim and Alex Walczak (Partner: cs61b-abo)
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** Another dummy test to avoid complaint. */
    @Test
    public void readFileTest() {
        File readf = new File(".testing_file");
        Main.createAndWriteFile(".testing_file", "61B ROCKS!!!");
        String res = Main.readFileContents(".testing_file");
        assertEquals(true, res.equals("61B ROCKS!!!"));
        assertEquals(false, res.equals("61B doesn't rock!!!"));
        readf.delete();
    }

    /** Another dummy test to avoid complaint. */
    @Test
    public void createFileTest() {
        purgeDirectory(new File(".gitlet"));
        File test = new File(".testing_file");
        assertEquals(false, test.isFile());
        Main.createAndWriteFile(".testing_file", "61B ROCKS!!!");
        assertEquals(true, test.isFile());
        test.delete();
    }

    /** Another dummy test to avoid complaint. */
    @Test
    public void initTest() {
        purgeDirectory(new File(".gitlet"));
        File stage = new File(".gitlet/staging");
        Main.main("init");
        assertEquals(true, stage.isDirectory());
        stage.delete();
        purgeDirectory(new File(".gitlet"));
    }

    /** Another dummy test to avoid complaint. */
    @Test
    public void addTest() {
        purgeDirectory(new File(".gitlet"));
        Main.main("init");
        File stage = new File(".gitlet/staging");
        File toAdd = new File(".toAdd");
        File toCheck = new File(".gitlet/staging/.toAdd");
        Main.createAndWriteFile(".toAdd", "a");
        String[] args = new String[]{"add", ".toAdd"};
        Main.main(args);
        assertEquals(true, toCheck.isFile());
        toCheck.delete();
        toAdd.delete();
        stage.delete();
        purgeDirectory(new File(".gitlet"));
    }

    /** Another dummy test to avoid complaint. */
    @Test
    public void rmTest() {
        purgeDirectory(new File(".gitlet"));
        File stage = new File(".gitlet/staging");
        File toAdd = new File(".toAdd");
        File toCheck = new File(".gitlet/staging/.toAdd");
        Main.createAndWriteFile(".toAdd", "a");
        String[] args = new String[]{"add", ".toAdd"};
        String[] args2 = new String[]{"rm", ".toAdd"};
        String[] args3 = new String[]{"commit", "MESSAGE"};
        assertEquals(true, toAdd.isFile());

        Main.main("init");
        Main.main(args);
        assertEquals(true, toCheck.isFile());

        Main.main(args2);
        assertEquals(true, toAdd.isFile());
        assertEquals(false, toCheck.isFile());

        Main.main(args);
        Main.main(args3);
        Main.main(args2);
        assertEquals(false, toCheck.isFile());
        assertEquals(false, toAdd.isFile());

        toCheck.delete();
        toAdd.delete();
        stage.delete();
        purgeDirectory(new File(".gitlet"));
    }

    /** Another dummy test to avoid complaint. */
    @Test
    public void branchTest() {
        purgeDirectory(new File(".gitlet"));
        File newBranch1 = new File(".gitlet/currentbranch/master");
        File newBranch2 = new File(".gitlet/branches/boo");
        File newBranch3 = new File(".gitlet/currentbranch/boo");
        String[] args = new String[]{"branch", "boo"};
        String[] args2 = new String[]{"checkout", "boo"};

        Main.main("init");
        assertEquals(true, newBranch1.isDirectory());

        Main.main(args);
        assertEquals(true, newBranch2.isDirectory());
        assertEquals(false, newBranch3.isDirectory());

        Main.main(args2);
        assertEquals(false, newBranch2.isDirectory());
        assertEquals(true, newBranch3.isDirectory());

        newBranch1.delete();
        newBranch2.delete();
        newBranch3.delete();
        purgeDirectory(new File(".gitlet"));
    }

    /** Another dummy test to avoid complaint. */
    @Test
    public void rmBranchTest() {
        purgeDirectory(new File(".gitlet"));
        File newBranch1 = new File(".gitlet/currentbranch/master");
        File newBranch2 = new File(".gitlet/branches/boo");
        File newBranch3 = new File(".gitlet/currentbranch/boo");
        String[] args = new String[]{"branch", "boo"};
        String[] args2 = new String[]{"rm-branch", "boo"};

        Main.main("init");
        assertEquals(true, newBranch1.isDirectory());

        Main.main(args);
        assertEquals(true, newBranch2.isDirectory());
        assertEquals(false, newBranch3.isDirectory());

        Main.main(args2);
        assertEquals(false, newBranch3.isFile());
        assertEquals(false, newBranch2.isFile());

        newBranch1.delete();
        newBranch2.delete();
        newBranch3.delete();
        purgeDirectory(new File(".gitlet"));
    }

    /** This method DIR and
     *  See: http://stackoverflow.com/questions/
     *  13195797/delete-all-files-in-directory
     *  -but-not-directory-one-liner-solution. */
    public static void purgeDirectory(File dir) {
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    purgeDirectory(file);
                }
                file.delete();
            }
            dir.delete();
        }
    }

}
