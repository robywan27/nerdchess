package it.polimi.group02.model.piece;

import it.polimi.group02.model.Piece;
import it.polimi.group02.model.utility.Direction;
import it.polimi.group02.model.utility.MoveType;
import it.polimi.group02.model.utility.Range;
import it.polimi.group02.model.utility.Strength;

import static it.polimi.group02.model.utility.Utility.archer_initial_vitality;


/**
 * This class extends and implements the abstract class Piece. It represents an archer piece.
 */
public class Archer extends Piece {

    /**
     * Constructor.
     */
    public Archer(char pieceSymbol, int xCoordinate, int yCoordinate) {
        super(pieceSymbol, xCoordinate, yCoordinate);
        this.setVitality(archer_initial_vitality);
        this.setMoveRange(Range.MEDIUM);
        this.setMoveDirection(Direction.ANY);
        this.setAttackDirection(Direction.HORIZONTAL_VERTICAL);
        this.setAttackRange(Range.LONG);
        this.setMoveType(MoveType.WALK);
        this.setAttackStrength(Strength.MEDIUM);
    }

}