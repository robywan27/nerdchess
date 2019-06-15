package it.polimi.group02.model.piece;

import it.polimi.group02.model.Piece;
import it.polimi.group02.model.utility.Direction;
import it.polimi.group02.model.utility.MoveType;
import it.polimi.group02.model.utility.Range;
import it.polimi.group02.model.utility.Strength;

import static it.polimi.group02.model.utility.Utility.giant_initial_vitality;

/**
 * This class extends and implements the abstract class Piece. It represents a giant piece.
 */
public class Giant extends Piece {

    /**
     * Constructor.
     */
	public Giant(char pieceSymbol, int xCoordinate, int yCoordinate) {
		super(pieceSymbol, xCoordinate, yCoordinate);
		this.setVitality(giant_initial_vitality);
		this.setMoveDirection(Direction.HORIZONTAL_VERTICAL);
		this.setAttackDirection(Direction.HORIZONTAL_VERTICAL);
		this.setAttackRange(Range.SHORT);
		this.setMoveRange(Range.MEDIUM);
		this.setMoveType(MoveType.WALK);
		this.setAttackStrength(Strength.STRONGEST);
	}

}