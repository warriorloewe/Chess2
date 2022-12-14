package Pieces;

import Game.GameEnvironment;
import Game.Square;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(GameEnvironment ge, boolean white, int position) {
        super(ge, white, "queen", 9, position, 0, 7);
    }

    @Override
    public ArrayList<Square> getReachableFields(Square[] board) {
        return this.goDirections(board);
    }
}
