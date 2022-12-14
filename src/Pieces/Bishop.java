package Pieces;

import Game.GameEnvironment;
import Game.Square;
import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(GameEnvironment ge, boolean white, int position) {
        super(ge, white, "bishop", 3, position, 0, 3);
    }

    @Override
    public ArrayList<Square> getReachableFields(Square[] board) {
        return this.goDirections(board);
    }
}
