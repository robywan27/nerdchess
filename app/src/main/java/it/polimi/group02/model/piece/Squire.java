package it.polimi.group02.model.piece;

import it.polimi.group02.model.Piece;
import it.polimi.group02.model.utility.Direction;
import it.polimi.group02.model.utility.MoveType;
import it.polimi.group02.model.utility.Range;
import it.polimi.group02.model.utility.Strength;

import static it.polimi.group02.model.utility.Utility.squire_initial_vitality;

/**
 * This class extends and implements the abstract class Piece. It represents a squire piece.
 */
public class Squire extends Piece {

    /**
     * Constructor.
     */
	public Squire(char pieceSymbol, int xCoordinate, int yCoordinate) {
		super(pieceSymbol, xCoordinate, yCoordinate);
        this.setVitality(squire_initial_vitality);
        this.setMoveDirection(Direction.HORIZONTAL_VERTICAL);
		this.setAttackDirection(Direction.NOT_AVAILABLE);
		this.setAttackRange(Range.NOT_AVAILABLE);
		this.setMoveRange(Range.SHORT);
		this.setMoveType(MoveType.WALK);
		this.setAttackStrength(Strength.WEAK);
	}

}