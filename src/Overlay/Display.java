package Overlay;

import Game.GameEnvironment;
import Game.Square;
import Library.Button;
import Library.ButtonBox;
import Library.Menu;
import Library.Textfield;
import Pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Display extends JComponent {
    Menu mainMenu;
    Menu settingsMenu;
    Menu boardMenu;
    GameEnvironment ge;

    Color lightSquares = new Color(235, 229, 205);
    Color darkSquares = new Color(142, 138, 119);
    Color specialSquare = new Color(108, 168, 62);         // used for selected squares and squares u can "attack"

    public Display(GameEnvironment ge) {
        this.ge = ge;
        initMenus();
        ge.setActiveMenu(mainMenu);
    }

    public void paint(Graphics g) {
        switch (GameEnvironment.gameState) {
            case MAIN_MENU -> mainMenu.paint(g);
            case SETTINGS -> settingsMenu.paint(g);
            case BOARD -> {
                g.setColor(Color.GRAY);
                g.fillRect(0, 0, GUI.window.width, GUI.window.height);
                drawBoard(g);
                if(ge.isWhitesMove()) {
                    boardMenu.getTextFields().get(0).setText(ge.getWhiteTime());
                } else {
                    boardMenu.getTextFields().get(1).setText(ge.getBlackTime());
                }
                boardMenu.paint(g);
            }
        }
    }

    public void drawBoard(Graphics g) {
        int sq = GUI.squareSize;
        for (Square s : ge.getBoard()) {
            int x = s.getX() * sq + GUI.xOffset;
            int y = s.getY() * sq + GUI.yOffset;

            // if row + column is divisible by two the color is white
            if(s == ge.getSelectedSquare() || s.isAttackable()) {
                g.setColor(specialSquare);
            } else {
                g.setColor((x + y) % 2 == 0 ? lightSquares : darkSquares);
            }
            g.fillRect(x, y, sq, sq);

            if (s.isMarked() || s.isEnPassant()) {
                g.setColor(Color.DARK_GRAY);
                int diameter = sq / 4;
                g.fillOval(x + (sq - diameter) / 2, y + (sq - diameter) / 2, diameter, diameter);
            } else if (s.isAttackable()) {
                g.setColor((x + y) % 2 == 0 ? lightSquares : darkSquares);
                g.fillOval(x, y, sq, sq);
            }
            Piece p = s.getPiece();
            if (p != null) {
                g.drawImage(p.getImage(), x, y, sq, sq, null);
            }
        }
    }

    public void initMenus() {
        // main menu initialization
        String[] buttonImageNames = {"PlayButton", "PlayButtonSelected", "OptionsButton", "OptionsButtonSelected", "QuitButton", "QuitButtonSelected"};
        BufferedImage[] buttonImages = new BufferedImage[6];
        try {
            int i = 0;
            for (String s : buttonImageNames) {
                buttonImages[i] = ImageIO.read(new File("rsc/menu/" + s + ".png"));
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainMenu = new Menu();
        ButtonBox mmBB = new ButtonBox(1, 3);
        // play button
        mmBB.replaceButton(0, 0, new Button(0.32, 0.5, 0.36, 0.12, buttonImages[0], buttonImages[1], () -> {
            // standard starting position
            ge.createBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
            GameEnvironment.gameState = GameEnvironment.GameState.BOARD;
            ge.setActiveMenu(boardMenu);
        }));
        // settings button
        mmBB.replaceButton(0, 1, new Button(0.4, 0.64, 0.2, 0.07, buttonImages[2], buttonImages[3], () -> {

        }));
        // quit button
        mmBB.replaceButton(0, 2, new Button(0.4, 0.73, 0.2, 0.07, buttonImages[4], buttonImages[5], () -> GUI.getInstance().endGame()));
        mainMenu.setButtonBox(mmBB);

        // board menu initialization
        boardMenu = new Menu();
        Textfield whiteTimer = new Textfield(0.775, 0.5, 0.2, 0.1, ge.getWhiteTime());
        Textfield blackTimer = new Textfield(0.775, 0.4, 0.2, 0.1, ge.getBlackTime());
        whiteTimer.setVisible(true);
        blackTimer.setVisible(true);
        boardMenu.addTextField(whiteTimer);
        boardMenu.addTextField(blackTimer);
    }
}
