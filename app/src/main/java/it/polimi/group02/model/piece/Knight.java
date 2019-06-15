package it.polimi.group02.model.piece;

import it.polimi.group02.model.Piece;
import it.polimi.group02.model.utility.Direction;
import it.polimi.group02.model.utility.MoveType;
import it.polimi.group02.model.utility.Range;
import it.polimi.group02.model.utility.Strength;

import static it.polimi.group02.model.utility.Utility.knight_initial_vitality;

/**
 * This class extends and implements the abstract class Piece. It represents a knight piece.
 */
public class Knight extends Piece {

    /**
     * Constructor.
     */
    public Knight(char pieceSymbol, int xCoordinate, int yCoordinate) {
        super(pieceSymbol, xCoordinate, yCoordinate);
        this.setVitality(knight_initial_vitality);
        this.setMoveDirection(Direction.ANY);
        this.setAttackDirection(Direction.DIAGONAL);
        this.setAttackRange(Range.SHORT);
        this.setMoveRange(Range.SHORT);
        this.setMoveType(MoveType.WALK);
        this.setAttackStrength(Strength.MEDIUM);
    }

}