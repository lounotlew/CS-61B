package canfield;

import ucb.gui.TopLevel;
import ucb.gui.LayoutSpec;

import java.awt.event.MouseEvent;

/** A top-level GUI for Canfield solitaire.
 *  @author P. N. Hilfinger
 *  @author Woo Sik (Lewis) Kim
 */
class CanfieldGUI extends TopLevel {

    /** A new window with given TITLE and displaying GAME. */
    CanfieldGUI(String title, Game game) {
        super(title, true);
        _game = game;
        addLabel("Welcome to Canfield.",
                 new LayoutSpec("y", 0, "x", 0));
        addButton("Undo", "undo", new LayoutSpec("y", 0, "x", 2));
        addButton("Quit", "quit", new LayoutSpec("y", -1, "x", 2));
        addButton("Score", "score", new LayoutSpec("y", 0, "x", 3));

        _display = new GameDisplay(game);
        add(_display, new LayoutSpec("y", 2, "width", 2));
        _display.setMouseHandler("click", this, "mouseClicked");
        _display.setMouseHandler("release", this, "mouseReleased");
        _display.setMouseHandler("drag", this, "mouseDragged");
        _display.setMouseHandler("press", this, "mousePressed");

        display(true);
    }

    /** Respond to "Quit" button. */
    public void quit(String dummy) {
        System.exit(1);
    }

    /** Respond to "Undo" button. */
    public void undo(String dummy) {
        _game.undo();
        _display.repaint();
    }

    /** Respond to "Score" button. */
    public void score(String dummy) {
        System.out.println("Your current score is " + _game.getScore() + ".");
    }

    /** Action in response to mouse-clicking event EVENT. */
    public synchronized void mouseClicked(MouseEvent event) {
        int clickedX = event.getX(), clickedY = event.getY();
        if (eventOnStock(clickedX, clickedY)) {
            _game.stockToWaste();
            _display.repaint();
        } else {
            return;
        }
    }

    /** Action in response to a mouse-pressed event EVENT. */
    public synchronized void mousePressed(MouseEvent event) {
        _pressedX = event.getX();
        _pressedY = event.getY();
    }

    /** Action in response to mouse-released event EVENT. */
    public synchronized void mouseReleased(MouseEvent event) {
        _releasedX = event.getX();
        _releasedY = event.getY();

        mousePressedReserve(_pressedX, _pressedY, _releasedX, _releasedY);
        mousePressedWaste(_pressedX, _pressedY, _releasedX, _releasedY);
        mousePressedFoundation(_pressedX, _pressedY, _releasedX,
             _releasedY);
        mousePressedTableau(_pressedX, _pressedY, _releasedX, _releasedY);

        ifPlayerWonGame();

    }

    /** Action in response to mouse-moving event EVENT. Implemented in
     * mousePressed and mouseReleased. */
    public synchronized void mouseMoved(MouseEvent event) {

    }

    /** Action in response to mouse-dragging event EVENT. Implemented in
     * mousePressed and mouseReleased. */
    public synchronized void mouseDragged(MouseEvent event) {

    }

    /** Checks to see if the mouse-pressing event at PRESSEDX and PRESSEDY
     * occured at the Reserve pile; if true, it checks to see if the
     * mouse-releasing event at RELEASEDX and RELEASEDY occured
     * at the Foundation pile or the Tableau pile. If the mouse-releasing
     * event did occur at either of these piles, it then checks to see
     * if it can run the appropriate move and makes it if possible. */
    private void mousePressedReserve(int pressedX, int pressedY,
         int releasedX, int releasedY) {
        if (eventOnReserve(pressedX, pressedY)) {
            if (eventOnFoundation(releasedX, releasedY)) {
                _game.reserveToFoundation();
                _display.repaint();
            } else if (eventOnTableau(releasedX, releasedY)) {
                _releasedTableauPile = tableauPileNumber(releasedX, releasedY);
                _game.reserveToTableau(_releasedTableauPile);
                _display.repaint();
            }
        }
    }

    /** Checks to see if the mouse-pressing event at PRESSEDX and PRESSEDY
     * occured at the Waste pile; if true, it checks to see if the
     * mouse-releasing event at RELEASEDX and RELEASEDY occured
     * at the Foundation pile or the Tableau pile. If the mouse-releasing
     * event occured at any of these piles, then it checks to see if it
     * can make the appropriate move and makes it if possible. */
    private void mousePressedWaste(int pressedX, int pressedY,
         int releasedX, int releasedY) {
        if (eventOnWaste(pressedX, pressedY)) {
            if (eventOnFoundation(releasedX, releasedY)) {
                _game.wasteToFoundation();
                _display.repaint();
            } else if (eventOnTableau(releasedX, releasedY)) {
                _releasedTableauPile = tableauPileNumber(releasedX,
                     releasedY);
                _game.wasteToTableau(_releasedTableauPile);
                _display.repaint();
            }
        }
    }

    /** Checks to see if the mouse-pressing event at PRESSEDX and PRESSEDY
     * occured at the Tableau pile; if true, it checks to see if the
     * mouse-releasing event at RELEASEDX and RELEASEDY occured at any of
     * the Foundation piles. If so, it then checks to see if the
     * mouse-releasing event occured at the Tableau piles. If it did, then
     * it checks to see if it can make the appropriate move
     * and makes it if possible. */
    private void mousePressedFoundation(int pressedX, int pressedY,
         int releasedX, int releasedY) {
        if (eventOnFoundation(pressedX, pressedY)) {
            _pressedFoundationPile = foundationPileNumber(pressedX,
                 pressedY);
            if (eventOnTableau(_releasedX, _releasedY)) {
                _releasedTableauPile = tableauPileNumber(releasedX,
                    releasedY);
                _game.foundationToTableau(_pressedFoundationPile,
                    _releasedTableauPile);
                _display.repaint();
            }
        }
    }

    /** Checks to see if the mouse-pressing event at PRESSEDX and PRESSEDY
     * occured at the Tableau piles; if true, it checks to see if the
     * mouse-releasing event at RELEASEDX and RELEASEDY occured at the
     * Tableau piles or the Foundation piles. If the mouse-releasing event
     * occured at any of these piles, it then checks to see if it can make
     * the appropriate move and makes it if possible. */
    private void mousePressedTableau(int pressedX, int pressedY,
         int releasedX, int releasedY) {
        if (eventOnTableau(pressedX, pressedY)) {
            _pressedTableauPile = tableauPileNumber(pressedX, pressedY);
            if (eventOnFoundation(releasedX, releasedY)) {
                _game.tableauToFoundation(_pressedTableauPile);
                _display.repaint();
            } else if (eventOnTableau(releasedX, releasedY)) {
                _releasedTableauPile = tableauPileNumber(releasedX,
                     releasedY);
                _game.tableauToTableau(_pressedTableauPile,
                     _releasedTableauPile);
                _display.repaint();
            }
        }
    }

    /** Returns which Foundation pile a mouse event at X and Y occured at.
     * If the mouse event did not occur at a Foundation pile, returns 0. */
    private int foundationPileNumber(int x, int y) {
        if (x >= _foundationXcoor[0] && x <= _foundationXcoor[0]
            + _cWidth && y >= _foundationY && y <= _foundationY + _cHeight) {
            return 1;
        } else if (x >= _foundationXcoor[1] && x <= _foundationXcoor[1]
             + _cWidth && y >= _foundationY && y <= _foundationY + _cHeight) {
            return 2;
        } else if (x >= _foundationXcoor[2] && x <= _foundationXcoor[2]
             + _cWidth && y >= _foundationY && y <= _foundationY
             + _cHeight) {
            return 3;
        } else if (x >= _foundationXcoor[3] && x <= _foundationXcoor[3]
             + _cWidth && y >= _foundationY && y <= _foundationY + _cHeight) {
            return 4;
        } else {
            return 0;
        }
    }

    /** Returns which Tableau pile a mouse event at X and Y occured at.
     * If the mouse event did not occur at a Tableau pile, returns 0. */
    private int tableauPileNumber(int x, int y) {
        _numCardsTableau1 = _game.tableauSize(1) - 1;
        _numCardsTableau2 = _game.tableauSize(2) - 1;
        _numCardsTableau3 = _game.tableauSize(3) - 1;
        _numCardsTableau4 = _game.tableauSize(4) - 1;

        if (x >= _tableauXcoor[0] && x <= _tableauXcoor[0] + _cWidth
             && y >= _tableauY && y <= _tableauY + _cHeight
             + _numCardsTableau1 * _delta) {
            return 1;
        } else if (x >= _tableauXcoor[1] && x <= _tableauXcoor[1]
             + _cWidth && y >= _tableauY && y <= _tableauY + _cHeight
             + _numCardsTableau2 * _delta) {
            return 2;
        } else if (x >= _tableauXcoor[2] && x <= _tableauXcoor[2]
             + _cWidth && y >= _tableauY && y <= _tableauY
             + _cHeight + _numCardsTableau3 * _delta) {
            return 3;
        } else if (x >= _tableauXcoor[3] && x <= _tableauXcoor[3]
             + _cWidth && y >= _tableauY && y <= _tableauY
             + _cHeight + _numCardsTableau4 * _delta) {
            return 4;
        } else {
            return 0;
        }
    }

    /** Returns TRUE if a mouse event at X and Y occured at the Reserve pile;
     * else, returns FALSE. */
    private boolean eventOnReserve(int x, int y) {
        if (x >= _reserveX && x <= _reserveX + _cWidth && y
             >= _reserveY && y <= _reserveY + _cHeight) {
            return true;
        }
        return false;
    }

    /** Returns TRUE if a mouse event at X and Y occured at the Stock pile;
     * else, returns FALSE. */
    private boolean eventOnStock(int x, int y) {
        if (x >= _stockX && x <= _stockX + _cWidth && y
             >= _stockY && y <= _stockY + _cHeight) {
            return true;
        }
        return false;
    }

    /** Returns TRUE if a mouse event at X and Y occured at the Waste pile;
     * else, returns FALSE. */
    private boolean eventOnWaste(int x, int y) {
        if (x >= _wasteX && x <= _wasteX + _cWidth && y
             >= _wasteY && y <= _wasteY + _cHeight) {
            return true;
        }
        return false;
    }

    /** Returns TRUE if a mouse event at X and Y occured at one of the
     * Foundation piles; else, returns FALSE. */
    private boolean eventOnFoundation(int x, int y) {
        if (foundationPileNumber(x, y) == 0) {
            return false;
        }
        return true;
    }

    /** Returns TRUE if a mouse event at X and Y occured at one of the
     * Tableau piles; else, returns FALSE. */
    private boolean eventOnTableau(int x, int y) {
        if (tableauPileNumber(x, y) == 0) {
            return false;
        }
        return true;
    }

    /** Checks to see if the game is won. */
    private void ifPlayerWonGame() {
        if (_game.isWon()) {
            System.out.println("Congratulations! You won the game!");
            _display.repaint();
        }
        return;
    }

    /** === Instance variables === */

    /** The board widget. */
    private final GameDisplay _display;

    /** The game I am consulting. */
    private final Game _game;

    /** A card's width and height (length of x and y). */
    private final int _cWidth = 90, _cHeight = 125;

    /** Coordinates of the Reserve pile.. */
    private final int _reserveX = 50, _reserveY = 210;
    /** Coordinates of the Stock pile. */
    private final int _stockX = 50, _stockY = 410;
    /** Coordinates of the Waste pile. */
    private final int _wasteX = 160, _wasteY = 410;
    /** X Coordinates of the Foundation piles in an array, from piles 1-4. */
    private final int[] _foundationXcoor = {315, 425, 535, 645};
    /** X Coordinates of the Tableau piles in an array, from piles 1-4. */
    private final int[] _tableauXcoor = {315, 425, 535, 645};
    /** Number of additional cards (not including first) in Tableau pile 1,
     * from piles 1-4. */
    private int _numCardsTableau1;
    /** Number of additional cards (not including first) in Tableau pile 2,
     * from piles 1-4. */
    private int _numCardsTableau2;
    /** Number of additional cards (not including first) in Tableau pile 3,
     * from piles 1-4. */
    private int _numCardsTableau3;
    /** Number of additional cards (not including first) in Tableau pile 4,
     * from piles 1-4. */
    private int _numCardsTableau4;
    /** Y Coordinate of all Foundation piles. */
    private final int _foundationY = 40;
    /** Y Coordinate of all Tableau piles. */
    private final int _tableauY = 210;
    /** Verical (Y) distance between each card in a tableau pile. */
    private final int _delta = 25;

    /** Coordinates of where a mouse-pressing event occurs. */
    private int _pressedX, _pressedY;
    /** Coordinates of where a mouse-releasing event occurs. */
    private int _releasedX, _releasedY;
    /** The Foundation piles where mouse-pressing and releasing events occur. */
    private int _pressedFoundationPile, _releasedFoundationPile;
    /** The Tableau piles where mouse-pressing and releasing events occur. */
    private int _pressedTableauPile, _releasedTableauPile;

}
