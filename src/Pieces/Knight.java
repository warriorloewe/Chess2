package Pieces;

import Game.GameEnvironment;
import Game.Square;

import java.util.ArrayList;

public class Knight extends Piece {

    private final int[][] jumps = {{-17, -1}, {-15, 1}, {-10, -2}, {-6, 2}, {6, -2},  {10, 2}, {15, -1}, {17, 1}};    // all the jumps a horse can make, the second number in each bracket is the y delta of the move

    public Knight(GameEnvironment ge, boolean white, int position) {
        super(ge, white, "knight", 3, position);
    }

    @Override
    public ArrayList<Square> getReachableFields(Square[] board) {
        reachableFields = new ArrayList<>();
        for(int[] jump : jumps) {
            int currentPosition = position + jump[0];
            if(!existingTargetSquare(currentPosition, jump[1])) {
                continue;
            }
            if(canGo(currentPosition)) {
                reachableFields.add(ge.getBoard()[currentPosition]);
            }
        }
        return reachableFields;
    }
}
