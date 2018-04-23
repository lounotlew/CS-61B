package loa;

import java.util.ArrayList;
import java.util.Iterator;

import static loa.Piece.*;
import static loa.Direction.*;
import static loa.Main.*;

/** An automated Player.
 *  @author Woo Sik (Lewis) Kim
 */
class MachinePlayer extends Player {

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
        _board = game.getBoard();
        _aiTurn = side;
        if (side == BP) {
            _opponent = WP;
        } else {
            _opponent = BP;
        }
    }

    /** Return the best legal move for this MachinePlayer. */
    @Override
    Move makeMove() {
        return findBestMove(_aiTurn, 1, _gameLost, _gameWon, _board);
    }

    /** Return the best possible move for SIDE at this state of BOARD, using
     *  the minimax algorithm. ALPHA and BETA are the max/min values used
    *  for the alpha-beta search, and DEPTH is how deep to search. */
    Move findBestMove(Piece side, int depth,
            int alpha, int beta, Board board) {

        Move bestMove = null;
        Board nextBoard = new Board(board);
        int bestValue = _gameLost;
        int currentValue = _gameWon;

        Iterator<Move> iter = nextBoard.iterator();
        for (Move nextMove = iter.next(); iter.hasNext();
                nextMove = iter.next()) {
            System.out.println(nextMove);
            nextBoard.makeMove(nextMove);
            currentValue = alphaBetaSearch(side.opposite(), nextBoard,
                alpha, beta, depth - 1);
            if (currentValue > bestValue) {
                bestMove = nextMove;
                bestValue = currentValue;
            }
            nextBoard.retract();
        }
        if (side.abbrev().equals("b")) {
            System.out.println("B::" + bestMove);
        } else if (side.abbrev().equals("w")) {
            System.out.println("W::" + bestMove);
        }
        return bestMove;
    }

    /** Return the best value after an alpha-beta search of the game tree
     *  of SIDE in the current state of BOARD, with ALPHA being the max
     *  value and BETA being the min value. DEPTH is the DEPTH of the
     *  alpha-beta search. */
    int alphaBetaSearch(Piece side, Board board,
            int alpha, int beta, int depth) {
        int score = 0;
        if (board.gameOver() || depth <= 0) {
            return setValue(board);
        }
        Board nextBoard = new Board(board);
        Iterator<Move> iter = nextBoard.iterator();
        ArrayList<Board> boardStates = new ArrayList<Board>();

        while (iter.hasNext()) {
            Move tempMove = iter.next();
            boardStates.add(nextBoard);
            nextBoard.retract();
        }

        if (board.turn() == _aiTurn) {
            for (Board boardState : boardStates) {
                score = alphaBetaSearch(_aiTurn.opposite(), boardState,
                    alpha, beta, depth - 1);
                if (score > alpha) {
                    alpha = score;
                } else if (beta >= alpha) {
                    return alpha;
                }
            }
            return alpha;

        } else {
            for (Board boardState: boardStates) {
                score = alphaBetaSearch(_aiTurn.opposite(), boardState,
                    alpha, beta, depth - 1);
                if (score < beta) {
                    beta = score;
                } else if (alpha >= beta) {
                    return beta;
                }
            }
            return beta;
        }
    }

    /** Return a comparative integer value for the state of BOARD to
     *  evaluate the "goodness" of a move. */
    int setValue(Board board) {
        if (board.piecesContiguous(_aiTurn)) {
            return _gameWon;
        } else if (board.piecesContiguous(_opponent)) {
            return _gameLost;
        }
        ArrayList<int[]> aiPieces = new ArrayList<int[]>();
        ArrayList<int[]> opponentPieces = new ArrayList<int[]>();

        findPlayerPieces(board, aiPieces, opponentPieces);

        int aiPiecesDistance = findDistance(aiPieces);
        int opPiecesDistance = findDistance(opponentPieces);
        return opPiecesDistance - aiPiecesDistance;
    }

    /** Return the sum of the distance between all of SIDE's pieces
     *  on the board. */
    int findDistance(ArrayList<int[]> side) {
        int sum = 0;

        for (int i = 0; i < side.size(); i += 1) {
            for (int j = 0; j < side.size(); j += 1) {
                double x = Math.pow(side.get(i)[0] - side.get(j)[0], 2);
                double y = Math.pow(side.get(i)[1] - side.get(j)[1], 2);
                double twoPiecedList = Math.sqrt(x + y);
                sum += Math.round(twoPiecedList);
            }
        }
        return sum;
    }

    /** Find all the pieces MYTURN belonging to this MachinePlayer
     *  and the piece OPPONENT belong to the opponent player on
     *  this BOARD. */
    void findPlayerPieces(Board board, ArrayList<int[]> myturn,
            ArrayList<int[]> opponent) {
        int[] coor = new int[2];
        for (int r = 1; r <= 8; r += 1) {
            for (int c = 1; c <= 8; c += 1) {
                if (board.get(c, r) == _aiTurn) {
                    coor[0] = c;
                    coor[1] = r;
                    myturn.add(coor);
                } else if (board.get(c, r) == _opponent) {
                    coor[0] = c;
                    coor[1] = r;
                    opponent.add(coor);
                }
            }
        }
    }

    /** The game board. */
    private Board _board;
    /** The piece the AI plays. */
    private Piece _aiTurn;
    /** The piece the opponent plays. */
    private Piece _opponent;

    /** Initial values for lost game states, set at negative infinity. */
    private int _gameLost = Integer.MIN_VALUE;
    /** Initial values for won game states, set at positive infinity. */
    private int _gameWon = Integer.MAX_VALUE;

}
