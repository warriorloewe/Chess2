package Pieces;

import Game.GameEnvironment;
import Game.Square;

import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(GameEnvironment ge, boolean white, int position) {
        super(ge, white, "rook", 5, position, 4, 7);
    }

    @Override
    public ArrayList<Square> getReachableFields(Square[] board) {
        return this.goDirections(board);
    }
}
