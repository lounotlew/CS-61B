package loa;

import static org.junit.Assert.*;
import org.junit.Test;

import static loa.Piece.*;
import static loa.Direction.*;

/** Tests for the Move class.
 *  @author Woo Sik (Lewis) Kim
 */
public class MoveTest {

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MoveTest.class));
    }

    @Test
    public void testGetDirection() {
        Board testBoard = new Board(_init, WP);
        Move north = Move.create(4, 5, 4, 7, testBoard);
        assertEquals(N, north.getDirection());
        Move northE = Move.create(5, 5, 7, 7, testBoard);
        assertEquals(NE, northE.getDirection());
        Move east = Move.create(5, 5, 7, 5, testBoard);
        assertEquals(E, east.getDirection());
        Move southE = Move.create(5, 4, 7, 2, testBoard);
        assertEquals(SE, southE.getDirection());
        Move south = Move.create(5, 4, 5, 2, testBoard);
        assertEquals(S, south.getDirection());
        Move southW = Move.create(4, 4, 2, 2, testBoard);
        assertEquals(SW, southW.getDirection());
        Move west = Move.create(4, 4, 2, 4, testBoard);
        assertEquals(W, west.getDirection());
        Move northW = Move.create(4, 5, 2, 7, testBoard);
        assertEquals(NW, northW.getDirection());
    }

    /** An initial configuration for testing. */
    private final Piece[][] _init = {
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, BP,  EMP, BP,  BP,  EMP, BP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, BP,  EMP, WP,  WP,  EMP, BP,  EMP },
        { EMP, BP,  EMP, WP,  WP,  EMP, BP,  EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, BP,  EMP, BP,  BP,  EMP, BP,  EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
    };

}
