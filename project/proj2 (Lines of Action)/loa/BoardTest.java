package loa;

import static org.junit.Assert.*;
import org.junit.Test;

import static loa.Piece.*;
import static loa.Direction.*;

/** Tests for the Board class.
 *  @author Woo Sik (Lewis) Kim
 */
public class BoardTest {

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(BoardTest.class));
    }

    @Test
    public void testPiecesContiguous() {
        Board board0 = new Board(_init, WP);
        assertEquals(12, board0.countNumPieces(BP));
        assertEquals(12, board0.countNumPieces(WP));
        assertFalse(board0.piecesContiguous(BP));
        assertFalse(board0.piecesContiguous(WP));

        Board board1 = new Board(_test1, WP);
        assertEquals(11, board1.countNumPieces(BP));
        assertEquals(11, board1.countNumPieces(WP));
        assertFalse(board1.piecesContiguous(BP));
        assertEquals(11, board1.checkSides(WP, 1, 2, N));
        assertTrue(board1.piecesContiguous(WP));
    }

    @Test
    public void testPieceCountAlong() {
        Board board0 = new Board(_test1, WP);
        Move move0 = Move.create(2, 1, 2, 4, board0);
        assertEquals(4, board0.pieceCountAlong(move0));
        Move move1 = Move.create(2, 1, 8, 7, board0);
        assertEquals(5, board0.pieceCountAlong(move1));
        Move move2 = Move.create(2, 4, 6, 8, board0);
        assertEquals(4, board0.pieceCountAlong(move2));
    }

    @Test
    public void testBlocked() {
        Board board0 = new Board(_test1, WP);
        Move move0 = Move.create(2, 1, 2, 4, board0);
        assertFalse(board0.blocked(move0));
        Move move1 = Move.create(2, 1, 8, 7, board0);
        assertTrue(board0.blocked(move1));
        Move move2 = Move.create(2, 4, 6, 8, board0);
        assertFalse(board0.blocked(move2));
    }

    @Test
    public void testIsLegal() {
        Board board0 = new Board(_test1, WP);
        Move move0 = Move.create(2, 1, 2, 7, board0);
        assertFalse(board0.isLegal(move0));
        Move move1 = Move.create(7, 1, 5, 1, board0);
        assertTrue(board0.isLegal(move1));
        Move move2 = Move.create(2, 4, 2, 6, board0);
        assertFalse(board0.isLegal(move2));
    }

    /** The standard initial configuration for Lines of Action. */
    private final Piece[][] _init = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** An initial board configuration for testing. */
    private final Piece[][] _test1 = {
        { EMP, WP,  EMP, EMP, EMP, EMP, WP,  EMP },
        { EMP, WP,  WP,  EMP, WP,  WP,  EMP, EMP },
        { EMP, EMP, EMP, WP,  BP,  BP,  EMP, EMP },
        { EMP, BP,  EMP, BP,  WP,  WP,  BP,  EMP },
        { EMP, EMP, BP,  EMP, EMP, BP,  WP,  EMP },
        { EMP, EMP, EMP, BP,  BP,  WP,  EMP, EMP },
        { EMP, BP,  EMP, EMP, BP,  EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP }
    };

}
