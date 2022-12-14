package Game;

import Overlay.GUI;
import Pieces.Piece;

import java.awt.event.*;

public class InputManager implements MouseListener, MouseMotionListener, KeyListener {
    // the booleans indicate whether a certain key is pressed or not
    private boolean w;
    private boolean a;
    private boolean s;
    private boolean d;
    private boolean space;
    private boolean esc;
    private boolean wReleased = true;
    private boolean aReleased = true;
    private boolean sReleased = true;
    private boolean dReleased = true;
    private boolean spaceReleased = true;
    private boolean escReleased = true;

    private final GameEnvironment ge;

    public InputManager(GameEnvironment ge) {
        this.ge = ge;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_W -> {
                w = true;
                wReleased = false;
            }
            case KeyEvent.VK_A -> {
                a = true;
                aReleased = false;
            }
            case KeyEvent.VK_S -> {
                s = true;
                sReleased = false;
            }
            case KeyEvent.VK_D -> {
                d = true;
                dReleased = false;
            }
            case KeyEvent.VK_SPACE -> {
                space = true;
                if (spaceReleased) {
                    ge.getActiveMenu().perform();
                }
                spaceReleased = false;

            }
            case KeyEvent.VK_ESCAPE -> {
                esc = true;
                escReleased = false;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_W -> {
                w = false;
                wReleased = true;
            }
            case KeyEvent.VK_A -> {
                a = false;
                aReleased = true;
            }
            case KeyEvent.VK_S -> {
                s = false;
                sReleased = true;
            }
            case KeyEvent.VK_D -> {
                d = false;
                dReleased = true;
            }
            case KeyEvent.VK_SPACE -> {
                space = false;
                spaceReleased = true;
            }
            case KeyEvent.VK_ESCAPE -> {
                esc = false;
                escReleased = true;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (GameEnvironment.gameState == GameEnvironment.GameState.BOARD && !ge.isGameOver()) {
            // gets the x and y coordinates from the position of the mousepress
            int x = (e.getX() - GUI.xOffset) / GUI.squareSize;
            int y = (e.getY() - GUI.yOffset) / GUI.squareSize;

            // if it is outside the bounds of the boards it ignores it
            if(x < 0 || y < 0 || x > 7 || y > 7) return;

            Square s = ge.getBoard()[x + y * 8];
            Piece p = s.getPiece();
            // if a square is marked moving there is definitely legal
            if (s.isMarked() || s.isAttackable() || s.isEnPassant()) {
                ge.move(ge.getSelectedPiece(), ge.getSelectedSquare(), s, s.isEnPassant(), false);
            } else if (p != null) {
                ge.updateSelectedPiece(p, s);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
