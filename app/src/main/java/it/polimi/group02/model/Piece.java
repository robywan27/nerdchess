package it.polimi.group02.model;

import it.polimi.group02.model.utility.Direction;
import it.polimi.group02.model.utility.MoveType;
import it.polimi.group02.model.utility.Range;
import it.polimi.group02.model.utility.Status;
import it.polimi.group02.model.utility.Strength;


/**
 * Each piece is identified by a symbol and its position in the fantasy_board, that is, by the tuple: (symbol, (x, y)). The symbol representing the
 * typology of the piece alone is not enough to distinguish two pieces of the same type, thus one piece can be identified if and only if
 * its position on the fantasy_board is considered.
 * This class is abstract because it defines a paradigm for each specific type of piece, that is, it defines a grouping of the common
 * elements of each subclass, but obviously cannot be instantiated.
 */
public abstract class Piece {
    /**
     * Each piece is symbolically identified by a letter: upper case stands for white player, lower case for black player. Nonetheless,
     * this attribute alone cannot allow to distinguish two or more piece of the same type belonging to the same player.
     */
	private char pieceSymbol;
    /**
     * This attribute stores the row position of the piece on the fantasy_board; as soon as a piece commits a move, this coordinate is updated
     * accordingly, leaving no room for ambiguity or mistake.
     */
	private int xCoordinate;
    /**
     * This attribute stores the column position of the piece on the fantasy_board; as soon as a piece commits a move, this coordinate is
     * updated accordingly, leaving no room for ambiguity or mistake.
     */
    private int yCoordinate;
    /**
     * This non-negative value represents the vitality possessed by a piece. It may decrease during the game, and when it reaches a
     * minimum of zero or below as a result of an attack or combat, the game is removed from the game.
     */
	private int vitality;
	/**
	 * This attribute defines the range of the move for a piece.
	 */
	private Range moveRange;
    /**
     * This attribute defines the legal directions for a piece.
     */
	private Direction moveDirection;
    /**
     * This attribute defines the type of move for a piece; it can be either walk, or fly.
     */
	private MoveType moveType;
    /**
     * This attribute defines the range of attack for a piece.
     */
	private Range attackRange;
    /**
     * This attribute defines the strength of a piece, that is, how much it can decrease one opponent's piece vitality.
     */
	private Strength attackStrength;
    /**
     * This attribute defines the attack direction for a piece.
     */
	private Direction attackDirection;
	/**
	 * This attribute holds the status of a piece, which can be any of the following: alive, frozen.
	 */
	private Status status;



    /**
     * Constructor.
     * @param pieceSymbol the symbol representing the type of piece
     * @param xCoordinate the row position of the piece
     * @param yCoordinate the column position of the piece
     */
	public Piece(char pieceSymbol, int xCoordinate, int yCoordinate) {
		this.pieceSymbol = pieceSymbol;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
		status = Status.ACTIVE;
	}



    /**
     * Override equals and hashCode methods in order to guarantee equality of objects in testing. The motivation for this is that
     two distinct objects have a different hashcode, although they may have the same values for the attributes. Thus,
     you need to override the equals method to redefine the equality relation for the objects. Plus, hashCode must always be
     overridden whenever equals is overridden.
     */
    @Override
    public boolean equals(Object other){
        if(this == other) return true;

        if(other == null || (this.getClass() != other.getClass())){
            return false;
        }

        Piece piece = (Piece) other;
        return this.pieceSymbol == piece.pieceSymbol && xCoordinate == piece.xCoordinate && xCoordinate == piece.xCoordinate;
    }

    @Override
    public int hashCode(){
        int result = 0;
        final int prime = 89;
        result = prime * result + pieceSymbol;
        result = prime * result + (int) (this.xCoordinate ^ (this.xCoordinate >>> 32));
        result = prime * result + (int) (this.yCoordinate ^ (this.yCoordinate >>> 32));

        return result;
    }


	/**
	 * Getter, setter methods.
     */
	public char getPieceSymbol() {
		return pieceSymbol;
	}

	public Range getMoveRange() {
		return moveRange;
	}

	public void setMoveRange(Range moveRange) {
		this.moveRange = moveRange;
	}

	public Direction getMoveDirection() {
		return moveDirection;
	}

	public void setMoveDirection(Direction moveDirection) {
		this.moveDirection = moveDirection;
	}

	public MoveType getMoveType() {
		return moveType;
	}

	public void setMoveType(MoveType moveType) {
		this.moveType = moveType;
	}

	public Range getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(Range attackRange) {
		this.attackRange = attackRange;
	}

	public Strength getAttackStrength() {
		return attackStrength;
	}

	public void setAttackStrength(Strength attackStrength) {
		this.attackStrength = attackStrength;
	}

	public Direction getAttackDirection() {
		return attackDirection;
	}

	public void setAttackDirection(Direction attackDirection) {
		this.attackDirection = attackDirection;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getVitality() {
		return vitality;
	}

	public void setVitality(int vitality) {
		this.vitality = vitality;
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
}