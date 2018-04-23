package canfield;

import ucb.gui.Pad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.imageio.ImageIO;

import java.io.InputStream;
import java.io.IOException;

/** A widget that displays a Pinball playfield.
 *  @author P. N. Hilfinger
 *  @author Woo Sik (Lewis) Kim
 */

class GameDisplay extends Pad {

    /** Color of display field. */
    private static final Color BACKGROUND_COLOR = Color.white;

    /* Coordinates and lengths in pixels unless otherwise stated. */

    /** Preferred dimensions of the playing surface. */
    private static final int BOARD_WIDTH = 800, BOARD_HEIGHT = 600;

    /** Displayed dimensions of a card image. */
    private static final int CARD_HEIGHT = 125, CARD_WIDTH = 90;

    /** A graphical representation of GAME. */
    public GameDisplay(Game game) {
        _game = game;
        setPreferredSize(BOARD_WIDTH, BOARD_HEIGHT);
    }

    /** Return an Image read from the resource named NAME. */
    private Image getImage(String name) {
        InputStream in =
            getClass().getResourceAsStream("/canfield/resources/" + name);
        try {
            return ImageIO.read(in);
        } catch (IOException excp) {
            return null;
        }
    }

    /** Return an Image of CARD. */
    private Image getCardImage(Card card) {
        return getImage("playing-cards/" + card + ".png");
    }

    /** Return an Image of the back of a card. */
    private Image getBackImage() {
        return getImage("playing-cards/blue-back.png");
    }

    /** Draw CARD at X, Y on G. */
    private void paintCard(Graphics2D g, Card card, int x, int y) {
        if (card != null) {
            g.drawImage(getCardImage(card), x, y,
                        CARD_WIDTH, CARD_HEIGHT, null);
        }
    }

    /** Draw card back at X, Y on G. */
    private void paintBack(Graphics2D g, int x, int y) {
        g.drawImage(getBackImage(), x, y, CARD_WIDTH, CARD_HEIGHT, null);
    }

    /** Draw an empty card slot at X, Y on G. */
    private void paintEmpty(Graphics2D g, int x, int y) {
        g.drawImage(getImage("playing-cards/empty.png"), x, y,
                    CARD_WIDTH, CARD_HEIGHT, null);
    }

    /** Draw the #P Foundation pile at X and Y on G. */
    private void paintFoundationPile(Graphics2D g, int p, int x, int y) {
        if (_game.topFoundation(p) == null) {
            paintEmpty(g, x, y);
        }
        paintCard(g, _game.topFoundation(p), x, y);
    }

    /** Overloading for the paintTableauPile method below. Draws each card
     * on G in #K tableau pile that has SIZE cards. */
    private void overloadPaintTableau(Graphics2D g, int k, int size) {
        paintTableauPile(g, k, size - 1, _tableauXcoor[k - 1], _tableauY, size);
    }

    /** Draws the #K Tableau pile at X and Y on G, starting with the
     * #N card. Stops if the SIZE of the pile is 0 or if the #N card
     * is 0 (#N card = top card). */
    private void paintTableauPile(Graphics2D g, int k, int n, int x, int y,
        int size) {
        if (size == 0) {
            paintEmpty(g, x, y);
            return;
        } else if (n == 0) {
            paintCard(g, _game.topTableau(k), x, y);
            return;
        } else {
            paintCard(g, _game.getTableauCard(k, n), x, y);
            paintTableauPile(g, k, n - 1, x, y + _delta, size);
        }
    }

    /** Prints a 'Congratulations! You Win!' image at X and Y on G
     * if the game is won. */
    private void paintYouWin(Graphics2D g, int x, int y) {
        g.drawImage(getImage("youwin.png"), x, y, _youWinWidth,
            _youWinHeight, null);
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        g.setColor(BACKGROUND_COLOR);
        Rectangle b = g.getClipBounds();
        g.fillRect(0, 0, b.width, b.height);

        if (_game.topReserve() == null) {
            paintEmpty(g, _reserveX, _reserveY);
        }
        paintCard(g, _game.topReserve(), _reserveX, _reserveY);

        paintBack(g, _stockX, _stockY);

        if (_game.topWaste() == null) {
            paintEmpty(g, _wasteX, _wasteY);
        }
        paintCard(g, _game.topWaste(), _wasteX, _wasteY);

        paintFoundationPile(g, 1, _foundationXcoor[0], _foundationY);
        paintFoundationPile(g, 2, _foundationXcoor[1], _foundationY);
        paintFoundationPile(g, 3, _foundationXcoor[2], _foundationY);
        paintFoundationPile(g, 4, _foundationXcoor[3], _foundationY);

        overloadPaintTableau(g, 1, _game.tableauSize(1));
        overloadPaintTableau(g, 2, _game.tableauSize(2));
        overloadPaintTableau(g, 3, _game.tableauSize(3));
        overloadPaintTableau(g, 4, _game.tableauSize(4));

        if (_game.isWon()) {
            paintYouWin(g, _youWinCoor, _youWinCoor);
        }

    }

    /** === Instance variables === */

    /** Game I am displaying. */
    private final Game _game;

    /** A card's width and height (length of x and y). */
    private final int cWidth = 90, cHeight = 125;

    /** Coordinates of the Reserve pile.. */
    private final int _reserveX = 50, _reserveY = 210;
    /** Coordinates of the Stock pile. */
    private final int _stockX = 50, _stockY = 410;
    /** Coordinates of the Waste pile. */
    private final int _wasteX = 160, _wasteY = 410;
    /** X Coordinates of the Foundation piles, from piles 1-4. */
    private final int[] _foundationXcoor = {315, 425, 535, 645};
    /** X Coordinates of the Tableau piles in an array, from piles 1-4. */
    private final int[] _tableauXcoor = {315, 425, 535, 645};
    /** Y Coordinate of all Foundation piles. */
    private final int _foundationY = 40;
    /** Y Coordinate of all Tableau piles. */
    private final int _tableauY = 210;
    /** X and Y coordinates of the 'You Win' image. */
    private final int _youWinCoor = 60;
    /** Width and height of the 'You Win' image. */
    private final int _youWinWidth = 180, _youWinHeight = 103;

    /** The vertical distance between each card in a tableau pile to
     * create an overlapping (cascade) effect. */
    private final int _delta = 25;

}
