package Pieces;

import Game.GameEnvironment;
import Game.Square;

import java.util.ArrayList;

public class King extends Piece {
    public King(GameEnvironment ge, boolean white, int position) {
        super(ge, white, "king", 0, position);
    }

    @Override
    public ArrayList<Square> getReachableFields(Square[] board) {
        ArrayList<Square> reachableFields = new ArrayList<>();
        for(int[] direction : directions) {
            int currentPosition = position + direction[0];
            if(existingTargetSquare(currentPosition, direction[1]) && canGo(currentPosition)) {
                reachableFields.add(board[currentPosition]);
            }
        }
        return reachableFields;
    }

    public ArrayList<Square> getCastleFields(Square[] board) {
        ArrayList<Square> castleFields = new ArrayList<>();
        if(canCastleShort(board)) {
            castleFields.add(board[position + 2]);
        }
        if(canCastleLong(board)) {
            castleFields.add(board[position - 2]);
        }
        return castleFields;
    }

    public boolean canCastleLong(Square[] board) {
        boolean canCastle = white ? ge.canWhiteCasteLong() : ge.canBlackCasteLong();
        return canCastle && checkRank(board, -4, 1);
    }

    public boolean canCastleShort(Square[] board) {
        boolean canCastle = white ? ge.canWhiteCasteShort() : ge.canBlackCasteShort();
        return canCastle && checkRank(board, 3, -1);
    }

    // checks if the squares between rook and king are free and if the rook hasnt moved yet
    public boolean checkRank(Square[] board, int start, int direction) {
        Piece rook = board[position + start].getPiece();
        if (!(rook instanceof Rook)) {
            return false;
        }
        if (rook.hasMoved() || rook.isWhite() != white) {
            return false;
        }
        for (int i = start + direction; i != 0; i += direction) {
            if (board[position + i].getPiece() != null) {
                return false;
            }
        }
        return true;
    }
}
