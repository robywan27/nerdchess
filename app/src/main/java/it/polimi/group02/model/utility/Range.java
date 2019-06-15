package it.polimi.group02.model.utility;


/**
 * This enum represents the range of movement or attack for a piece.
 */
public enum Range {
	SHORT(1),
	MEDIUM(2),
	LONG(3),
	NOT_AVAILABLE(0);

    /**
     * It is the integer value corresponding to a given range.
     */
	private int value;


    /**
     * Enum (private) constructor
     * @param value range value
     */
	Range(int value) {
		this.value = value;
	}


    /**
     * Getter to retrieve the numeric value corresponding to a given range.
     * @return range value
     */
	public int getValue() {
		return value;
	}
}