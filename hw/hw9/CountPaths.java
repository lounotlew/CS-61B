import java.util.List;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.util.Scanner;

/** HW #9, problem B.  Path-counting problem (translated and adapted by
 *  M. Dynin).
 *  @author Woo Sik (Lewis) Kim
 */
public class CountPaths {

    /** Read in maze and print out number of paths. */
    public static void main(String[] ignored) {
    	ArrayList<String> strings = new ArrayList<String>();
    	Scanner reader = new Scanner(new InputStreamReader(System.in));
    	int m = reader.nextInt();
    	int n = reader.nextInt();
    	int r = reader.nextInt();
    	int c = reader.nextInt();

    	String s = reader.next();
    	for (int i = 0; i < m; i += 1) {
    		strings.add(reader.next());
    	}

        int num = countPaths(0, strings, s, r, c);
        System.out.println("There are " + num + " paths.");
        return;
    }

    /** Return the number of paths, starting from NUM (number of paths) of 0, to
     *  construct S from the list of Strings STRINGS. Starts from position R, C. */
    private static int countPaths(int num, ArrayList<String> strings, String s,
    	int r, int c) {
    	if (r == strings.size()) {
    		return 0;
    	} else if (s.equals("")) {
    		return 0;
    	} else if (c == strings.get(0).length()) {
    		return 0;
    	} else {

    		// Count the number of paths.

    		return num;
    	}
    }


}
