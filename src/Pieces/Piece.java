package Pieces;

import Game.GameEnvironment;
import Game.Square;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Piece {
    final boolean white;
    private final String name;

    // -9 -8 -7
    // -1  0  1
    //  7  8  9
    final int[][] directions = {{-9, -1}, {-7, 1}, {7, -1}, {9, 1}, {-8, 0}, {1, 1}, {8, 0}, {-1, -1}}; // first four directions are diagonal moves the other are straight the second number in each bracket is the y delta of the move
    int points;
    int position;
    boolean moved = false;
    private int startDirection;
    private int endDirection;
    ArrayList<Square> reachableFields;
    BufferedImage image;
    GameEnvironment ge;

    public Piece(GameEnvironment ge, boolean white, String name, int points, int position) {
        this.ge = ge;
        this.white = white;
        this.name = name;
        this.points = points;
        this.position = position;
        try {
            image = ImageIO.read(new File("rsc/pieces/" + getName() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Piece(GameEnvironment ge, boolean white, String name, int points, int position, int startDirection, int endDirection) {
        this(ge, white, name, points, position);
        this.startDirection = startDirection;
        this.endDirection = endDirection;
    }

    /*
     * moves all directions the piece can go on the game board
     * returns all possible moves (legal or illegal)
     * until it either hits the edge or another piece
     * with the move capturing the piece included
     */
    public ArrayList<Square> goDirections(Square[] board) {
        reachableFields = new ArrayList<>();
        for(int i = startDirection; i <= endDirection; i++) {
            int currentPositon = position + directions[i][0];
            int squaresWalked = 1;
            while(existingTargetSquare(currentPositon, directions[i][1] * squaresWalked)) {
                if(board[currentPositon].getPiece() == null) {
                    reachableFields.add(board[currentPositon]);
                } else {
                    if(canAttack(currentPositon)) {
                        reachableFields.add(board[currentPositon]);
                    }
                    break;
                }
                currentPositon += directions[i][0];
                squaresWalked++;
            }
        }
        return reachableFields;
    }

    public boolean canGo(int pos) {
        return ge.getBoard()[pos].getPiece() == null || canAttack(pos);
    }

    public boolean canAttack(int pos) {
        Piece p = ge.getBoard()[pos].getPiece();
        if(p != null) {
            return p.isWhite() != white;
        }
        return false;
    }

    // checks if the piece is enough squares away from the edge to make sure the actual target square exists
    public boolean existingTargetSquare(int endPosition, int squaresInYDirection) {
        int y = position % 8;
        return y + squaresInYDirection >= 0 && y + squaresInYDirection <= 7 && endPosition >= 0 && endPosition <= 63;
    }

    public abstract ArrayList<Square> getReachableFields(Square[] board);

    public String getName() {
        String color = white ? "white_" : "black_";
        return color + name;
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean isWhite() {
        return white;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean hasMoved() {
        return moved;
    }
}
