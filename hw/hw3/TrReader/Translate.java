/* Problem #2, HW3, CS61B */

import java.io.StringReader;
import java.io.IOException;

/** String translation.
 *  @author Jeff Schoner
 */
public class Translate {
     /** Return the String S, but with all characters that occur in FROM
     *  changed to the corresponding characters in TO. FROM and TO must
     *  have the same length. */
    static String translate(String S, String from, String to) {
        char[] buffer = new char[S.length()];
        try {
            TrReader tr = new TrReader(new StringReader(S), from, to);
            tr.read(buffer, 0, buffer.length);
            return new String(buffer);
        } catch (IOException e) { return null; }
    }
}
