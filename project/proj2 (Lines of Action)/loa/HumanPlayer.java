package loa;

/** A Player that prompts for moves and reads them from its Game.
 *  @author Woo Sik (Lewis) Kim
 */
class HumanPlayer extends Player {

    /** A HumanPlayer that plays the SIDE pieces in GAME.  It uses
     *  GAME.getMove() as a source of moves.  */
    HumanPlayer(Piece side, Game game) {
        super(side, game);
        _board = game.getBoard();
    }

    @Override
    Move makeMove() {
        return getGame().getMove();
    }

    /** Board for this HumanPlayer. */
    private Board _board;
    /** The piece this HumanPlayer plays. */
    private Piece _turn;

}
