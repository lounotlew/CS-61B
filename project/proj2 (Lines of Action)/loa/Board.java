package loa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Formatter;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Board.M;
import static loa.Direction.*;

/** Represents the state of a game of Lines of Action.
 *  @author Woo Sik (Lewis) Kim
 */
class Board implements Iterable<Move> {

    /** Size of a board. */
    static final int M = 8;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row-1][col-1]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is MxM.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        clear();
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        reset();
        _myBoard = new Piece[8][8];
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        reset();
        _moves.clear();
        _myBoard = new Piece[M][M];
        for (int r = 1; r <= M; r += 1) {
            for (int c = 1; c <= M; c += 1) {
                set(c, r, contents[r - 1][c - 1]);
            }
        }
        _turn = side;
    }

    /** Set me to the initial configuration. */
    void clear() {
        reset();
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        reset();
        if (board == this) {
            return;
        }
        _moves.clear();
        _moves.addAll(board._moves);
        _turn = board._turn;
        for (int r = 1; r <= M; r++) {
            for (int c = 1; c <= M; c++) {
                set(c, r, board.get(c, r));
            }
        }
    }

    /** Return the contents of column C, row R, where 1 <= C,R <= 8,
     *  where column 1 corresponds to column 'a' in the standard
     *  notation. */
    Piece get(int c, int r) {
        return _myBoard[r - 1][c - 1];
    }

    /** Return the contents of the square SQ.  SQ must be the
     *  standard printed designation of a square (having the form cr,
     *  where c is a letter from a-h and r is a digit from 1-8). */
    Piece get(String sq) {
        return get(col(sq), row(sq));
    }

    /** Return my current board configuration. */
    Piece[][] getConfig() {
        Piece[][] config = new Piece[8][8];
        for (int r = 1; r <= 8; r += 1) {
            for (int c = 1; c <= 8; c += 1) {
                config[r - 1][c - 1] = _myBoard[r - 1][c - 1];
            }
        }
        return config;
    }

    /** Return the column number (a value in the range 1-8) for SQ.
     *  SQ is as for {@link get(String)}. */
    int col(String sq) {
        if (!ROW_COL.matcher(sq).matches()) {
            throw new IllegalArgumentException("bad square designator");
        }
        return sq.charAt(0) - 'a' + 1;
    }

    /** Return the row number (a value in the range 1-8) for SQ.
     *  SQ is as for {@link get(String)}. */
    int row(String sq) {
        if (!ROW_COL.matcher(sq).matches()) {
            throw new IllegalArgumentException("bad square designator");
        }
        return sq.charAt(1) - '0';
    }

    /** Set the square at column C, row R to V, and make NEXT the next side
     *  to move, if it is not null. */
    void set(int c, int r, Piece v, Piece next) {
        _myBoard[r - 1][c - 1] = v;
        if (next != null) {
            _turn = next;
        }
    }

    /** Set the square at column C, row R to V. */
    void set(int c, int r, Piece v) {
        set(c, r, v, null);
    }

    /** Assuming isLegal(MOVE), make MOVE. */
    void makeMove(Move move) {
        assert isLegal(move);
        _moves.add(move);
        Piece replaced = move.replacedPiece();
        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();
        if (replaced != EMP) {
            set(c1, r1, EMP);
        }
        set(c1, r1, move.movedPiece());
        set(c0, r0, EMP);
        _turn = _turn.opposite();
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Move move = _moves.remove(_moves.size() - 1);
        Piece replaced = move.replacedPiece();
        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();
        Piece movedPiece = move.movedPiece();
        set(c1, r1, replaced);
        set(c0, r0, movedPiece);
        _turn = _turn.opposite();
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff MOVE is legal for the player currently on move. */
    boolean isLegal(Move move) {
        if (move == null) {
            return false;
        } else if (blocked(move)) {
            return false;
        } else if (move.length() != pieceCountAlong(move)) {
            return false;
        } else if (move.movedPiece() != _turn) {
            return false;
        }
        return true;
    }

    /** Return a sequence of all legal moves from this position. */
    Iterator<Move> legalMoves() {
        return new MoveIterator();
    }

    @Override
    public Iterator<Move> iterator() {
        return legalMoves();
    }

    /** Return true if there is at least one legal move for the player
     *  on move. */
    public boolean isLegalMove() {
        return iterator().hasNext();
    }

    /** Return true iff either player has all his pieces contiguous. */
    boolean gameOver() {
        return piecesContiguous(BP) || piecesContiguous(WP);
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        reset();
        int[] initCoor = firstPiece(side);
        int initRow = initCoor[0];
        int initCol = initCoor[1];
        int toCheck;

        if (side == BP) {
            toCheck = countNumPieces(BP);
        } else {
            toCheck = countNumPieces(WP);
        }

        int numContigPieces = checkSides(side, initRow, initCol, N);

        return numContigPieces == toCheck;
    }

    /** Return the coordinates of the first encounter of piece SIDE on the
     * board, starting from (1, 1) and moving upwards in rows and
     * rightwards in columns. */
    int[] firstPiece(Piece side) {
        int[] coor = new int[2];
        for (int r = 1; r < 9; r += 1) {
            for (int c = 1; c < 9; c += 1) {
                Piece p = get(c, r);
                if (p == side) {
                    coor[0] = r;
                    coor[1] = c;
                    break;
                }
            }
        }
        return coor;
    }

    /** Rotating to DIR (clockwise), return the number of pieces the surrounding
     *  piece SIDE at ROW, COL that are of the same type as SIDE. */
    int checkSides(Piece side, int row, int col, Direction dir) {
        mark(row, col);
        int numAdj = 1;

        while (dir != null) {
            int r = row + dir.dr;
            int c = col + dir.dc;
            if (inBounds(r, c)) {
                Piece p = get(c, r);
                if (p == side && !_checked[r - 1][c - 1]) {
                    dir = dir.succ();
                    numAdj = numAdj + checkSides(p, r, c, N);
                } else {
                    dir = dir.succ();
                }
            } else {
                dir = dir.succ();
            }
        }
        return numAdj;
    }

    /** Return the number of SIDE pieces on this board. */
    int countNumPieces(Piece side) {
        int num = 0;
        for (int r = 1; r < 9; r += 1) {
            for (int c = 1; c < 9; c += 1) {
                if (get(c, r) == side) {
                    num += 1;
                }
            }
        }
        return num;
    }

    /** Return true if the indices R and C are in bounds (both between 1 and 8,
     *  inclusive). */
    boolean inBounds(int r, int c) {
        if (r <= 8 && r >= 1 && c <= 8 && c >= 1) {
            return true;
        }
        return false;
    }

    /** Reset all the pieces on this board (change to unmarked). */
    void reset() {
        for (int r = 0; r < 8; r += 1) {
            for (int c = 0; c < 8; c += 1) {
                _checked[r][c] = false;
            }
        }
    }

    /** Mark the piece at ROW and COL as checked. */
    void mark(int row, int col) {
        _checked[row - 1][col - 1] = true;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        for (int r = 1; r < 9; r += 1) {
            for (int c = 1; c < 9; c += 1) {
                if (get(c, r) != b.get(c, r)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = M; r >= 1; r -= 1) {
            out.format("    ");
            for (int c = 1; c <= M; c += 1) {
                out.format("%s ", get(c, r).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return the number of pieces in the line of action indicated by MOVE. */
    public int pieceCountAlong(Move move) {
        int col = move.getCol0();
        int row = move.getRow0();
        Direction dir = move.getDirection();
        return pieceCountAlong(col, row, dir);
    }

    /** Return the number of pieces in the line of action in direction DIR and
     *  containing the square at column C and row R. */
    public int pieceCountAlong(int c, int r, Direction dir) {
        int count = -1;
        for (int r0 = r, c0 = c; inBounds(r0, c0); r0 += dir.dr, c0 += dir.dc) {
            if (get(c0, r0) != EMP) {
                count += 1;
            }
        }
        for (int r0 = r, c0 = c; inBounds(r0, c0); r0 -= dir.dr, c0 -= dir.dc) {
            if (get(c0, r0) != EMP) {
                count += 1;
            }
        }
        return count;
    }

    /** Return true iff MOVE is blocked by an opposing piece or by a
     *  friendly piece on the target square. */
    public boolean blocked(Move move) {
        int col = move.getCol0();
        int row = move.getRow0();
        Direction dir = move.getDirection();
        int moveLength = move.length();

        Piece origin = get(col, row);
        for (int i = 0; i < moveLength; i += 1, row += dir.dr, col += dir.dc) {
            if (!inBounds(col, row)) {
                return true;
            } else if (get(col, row) != origin && get(col, row) != EMP) {
                return true;
            }
        }
        if (!inBounds(col, row)) {
            return true;
        }
        if (get(col, row) == origin) {
            return true;
        }
        return false;
    }

    /** Print the contents of this board. */
    void printBoard() {
        System.out.println("===");
        for (int r = 8; r > 0; r -= 1) {
            String line = "    ";
            for (int c = 1; c < 9; c += 1) {
                if (get(c, r) == EMP) {
                    line += "-";
                } else if (get(c, r) == BP) {
                    line += "b";
                } else if (get(c, r) == WP) {
                    line += "w";
                }
                if (c < 8) {
                    line += " ";
                }
            }
            System.out.println(line);
        }
        if (_turn == BP) {
            System.out.println("Next move: black");
            System.out.println("===");
            return;
        } else {
            System.out.println("Next move: white");
            System.out.println("===");
            return;
        }
    }

    /** The standard initial configuration for Lines of Action. */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Initial configuration for marked pieces (whether the piece has
     *  been checked by piecesContiguous/checkSides or not). */
    private boolean[][] _checked = {
        {false, false, false, false, false, false, false, false},
        {false, false, false, false, false, false, false, false},
        {false, false, false, false, false, false, false, false},
        {false, false, false, false, false, false, false, false},
        {false, false, false, false, false, false, false, false},
        {false, false, false, false, false, false, false, false},
        {false, false, false, false, false, false, false, false},
        {false, false, false, false, false, false, false, false}
    };

    /** The current state of the board. */
    private Piece[][] _myBoard = new Piece[M][M];
    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;

    /** An iterator returning the legal moves from the current board.
     *  It first finds all possible legal moves from the current board for
     *  the appropriate turn, adds those moves to an ArrayList<Move>, then
     *  returns the iterator for that array list. */
    private class MoveIterator implements Iterator<Move> {
        /** Current piece under consideration. */
        private int _c, _r;
        /** Next direction of current piece to return. */
        private Direction _dir;
        /** Next move. */
        private Move _move;
        /** The current board. */
        private Board _board;
        /** List of possible moves. */
        private ArrayList<Move> _allLegalMoves;
        /** Iterator for _moves. */
        private Iterator<Move> _legalIter;

        /** A new move iterator for turn(). */
        MoveIterator() {
            _c = 1; _r = 1; _dir = N;
            _board = new Board(_myBoard, _turn);
            _allLegalMoves = new ArrayList<Move>();

            findAllLegalMoves();

            _legalIter = _allLegalMoves.iterator();
            if (_legalIter.hasNext()) {
                Move tempMove = _legalIter.next();
                _move = tempMove;
            } else {
                _move = null;
            }
        }

        @Override
        public boolean hasNext() {
            return _move != null;
        }

        @Override
        public Move next() {
            if (_move == null) {
                throw new NoSuchElementException("no legal move");
            }

            Move nextMove = _move;
            if (_legalIter.hasNext()) {
                Move tempMove = _legalIter.next();
                _move = tempMove;
            } else {
                _move = null;
            }
            return nextMove;
        }

        @Override
        public void remove() {
        }

        /** Find all legal moves for this board, and add them to
         *  _allLegalMoves. */
        void findAllLegalMoves() {
            for (int r = 1; r <= M; r += 1) {
                for (int c = 1; c <= M; c += 1) {
                    _dir = N;
                    if (get(c, r) == _turn) {
                        while (_dir != null) {
                            int numPieces =
                                pieceCountAlong(c, r, _dir);
                            Move move =
                                Move.create(c, r, numPieces, _dir, _board);
                            if (isLegal(move)) {
                                _allLegalMoves.add(move);
                            }
                            _dir = _dir.succ();
                        }
                    }
                }
            }
        }
    }
}
