package canfield;

import static org.junit.Assert.*;
import org.junit.Test;

/** Tests of the Game class.
 *  @author P. N. Hilfinger
 *  @author Woo Sik (Lewis) Kim
 */

public class GameTest {

    /** Example. */
    @Test
    public void testInitialScore() {
        Game g = new Game();
        g.deal();
        assertEquals(5, g.getScore());
    }

    @Test
    public void testUndo1() {
        Game g = new Game();
        g.deal();
        g.stockToWaste();
        g.undo();

        assertTrue(g.prevStatesEmpty());
    }

    @Test
    public void testUndo2() {
        Game g = new Game();
        g.deal();
        g.undo();

        assertTrue(g.prevStatesEmpty());
    }

    @Test
    public void testUndo3() {
        Game g = new Game();
        g.deal();
        g.stockToWaste();

        assertFalse(g.prevStatesEmpty());
    }

    @Test
    public void testUndo4() {
        Game g = new Game();
        g.seed(101001);
        g.deal();
        g.stockToWaste();
        Card topOfWaste = Card.toCard("QH");
        g.stockToWaste();
        Card topOfWaste2 = Card.toCard("5D");
        g.stockToWaste();
        g.undo();

        assertEquals(topOfWaste2, g.topWaste());

        g.undo();

        assertEquals(topOfWaste, g.topWaste());
    }

    @Test
    public void testUndo5() {
        Game g = new Game();
        g.seed(101001);
        g.deal();
        g.stockToWaste();
        g.undo();

        assertEquals(null, g.topWaste());
    }

}
