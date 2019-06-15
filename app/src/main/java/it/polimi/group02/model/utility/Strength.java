package it.polimi.group02.model.utility;


/**
 * This enum represents the strength of attack of a piece. The corresponding numeric values allow to decrement one piece's vitality.
 */
public enum Strength {
	WEAK(1),
	MEDIUM(2),
	STRONG(3),
	STRONGEST(4);

    /**
     * It is the integer value corresponding to a given strength value.
     */
	private int value;


    /**
     * Enum (private) constructor
     * @param value strength value
     */
	Strength(int value) {
		this.value = value;
	}


    /**
     * Getter value to retrieve the numeric value corresponding to a given strength value.
     * @return strength value
     */
	public int getValue() {
		return value;
	}
}