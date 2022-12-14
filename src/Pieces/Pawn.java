package Pieces;

import Game.GameEnvironment;
import Game.Square;

import java.util.ArrayList;

public class Pawn extends Piece {
    private boolean enPassant;
    int diagonalRight;
    int diagonalLeft;
    int direction;

    public Pawn(GameEnvironment ge, boolean white, int position) {
        super(ge, white, "pawn", 1, position);
        direction = white ? -8 : 8;
        diagonalRight = white ? -7 : 9;
        diagonalLeft = white ? -9 : 7;
    }

    @Override
    public ArrayList<Square> getReachableFields(Square[] board) {
        reachableFields = new ArrayList<>();

        int currentPosition = position + direction;
        if(!existingTargetSquare(currentPosition, 0)) {
            return reachableFields;
        }
        if(ge.getBoard()[currentPosition].getPiece() == null) {
            reachableFields.add(board[currentPosition]);
            currentPosition += direction;
            if(!moved && ge.getBoard()[currentPosition].getPiece() == null) {
                reachableFields.add(board[currentPosition]);
            }
        }
        if(existingTargetSquare(position + diagonalLeft, -1)) {
            if(canAttack(position + diagonalLeft)) {
                reachableFields.add(board[position + diagonalLeft]);
            }
        }
        if(existingTargetSquare(position + diagonalRight, 1)) {
            if(canAttack(position + diagonalRight)) {
                reachableFields.add(board[position + diagonalRight]);
            }
        }
        return reachableFields;
    }

    public ArrayList<Square> getEnPassantFields(Square[] board) {
        ArrayList<Square> enPassantFields = new ArrayList<>();

        if(checkEnPassant(board, -1)) {
            enPassantFields.add(board[position + diagonalLeft]);
        }
        if(checkEnPassant(board, 1)) {
            enPassantFields.add(board[position + diagonalRight]);
        }
        return enPassantFields;
    }

    public boolean checkEnPassant(Square[] board, int direction) {
        if(board[position + direction].getPiece() instanceof Pawn) {
            return ((Pawn) board[position + direction].getPiece()).enPassant();
        }
        return false;
    }

    public boolean enPassant() {
        return enPassant;
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }
}
