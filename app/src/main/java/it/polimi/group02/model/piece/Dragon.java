package it.polimi.group02.model.piece;

import it.polimi.group02.model.Piece;
import it.polimi.group02.model.utility.Direction;
import it.polimi.group02.model.utility.MoveType;
import it.polimi.group02.model.utility.Range;
import it.polimi.group02.model.utility.Strength;

import static it.polimi.group02.model.utility.Utility.dragon_initial_vitality;

/**
 * This class extends and implements the abstract class Piece. It represents a dragon piece.
 */
public class Dragon extends Piece {

    /**
     * Constructor.
     */
    public Dragon(char pieceSymbol, int xCoordinate, int yCoordinate) {
        super(pieceSymbol, xCoordinate, yCoordinate);
        this.setVitality(dragon_initial_vitality);
        this.setMoveDirection(Direction.HORIZONTAL_VERTICAL);
        this.setAttackDirection(Direction.HORIZONTAL_VERTICAL);
        this.setAttackRange(Range.MEDIUM);
        this.setMoveRange(Range.LONG);
        this.setMoveType(MoveType.FLIGHT);
        this.setAttackStrength(Strength.STRONG);
    }

}