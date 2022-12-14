package Game;

import Pieces.Piece;

public class Square {
    private final int x;
    private final int y;
    private final int position;
    private Piece piece;
    private boolean marked;
    private boolean attackable;
    private boolean enPassant;

    public Square(int position, Piece piece) {
        this.piece = piece;
        this.position = position;
        x = position % 8;
        y = position / 8;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getPosition() {
        return position;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public boolean isAttackable() {
        return attackable;
    }

    public void setAttackable(boolean attackable) {
        this.attackable = attackable;
    }

    public boolean isEnPassant() {
        return enPassant;
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }
}
