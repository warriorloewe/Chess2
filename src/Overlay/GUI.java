package Overlay;

import Game.GameEnvironment;
import Game.InputManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame {
    public static Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    public static int squareSize;
    public static int yOffset;
    public static int xOffset;
    public static GUI INSTANCE;
    GameEnvironment ge;

    public GUI() {
        int boardSize = (int) (window.height * 0.9);
        squareSize = boardSize / 8;
        yOffset = (window.height - boardSize) / 2;
        xOffset = (window.width - boardSize) / 2;

        this.setTitle("Chess 2: GOTY Edition");
        this.setSize(window.width, window.height);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        INSTANCE = this;

        ge = new GameEnvironment();

        Display display = new Display(ge);
        display.setBounds(window);
        display.setVisible(true);
        this.add(display);
        ge.setDisplay(display);

        InputManager i = new InputManager(ge);
        addKeyListener(i);
        addMouseListener(i);
        addMouseMotionListener(i);
        ge.setInput(i);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                endGame();
            }
        });

        Thread thread = new Thread(ge);
        thread.start();
    }

    public static GUI getInstance() {
        return INSTANCE;
    }

    public void endGame() {
        System.exit(0);
    }
}
