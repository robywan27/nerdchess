package it.polimi.group02.model;

import it.polimi.group02.model.utility.Color;

import static it.polimi.group02.model.utility.Utility.UNUSED_SPELLS;


/**
 * This class defines one player who plays one or more games. A player can have a username or not: if he has, his identity remains
 * as he plays several games, and can collect scores as he plays; if he hasn't, his identity is maintained for the played match
 * only, and he's identified by the color of the pieces he's playing with. Although in each game a player has a set of pieces at his
 * disposal, this class doesn't hold a list of pieces, since they are not an intrinsic property of a player; indeed, a player can classic_play
 * several matches alternating color.
 */
public class Player {
    /**
     * This is the most relevant feature of a player, which identifies and distinguishes him among other players.
     */
    private String name;
    /**
     * This attribute stores the color of the player during each match. It allows to distinguish him from his opponent holding another
     * color. The colors defined for a player in each game are either white or black.
     */
	private Color color;
    /**
     * This attribute holds the information about the player's frozen piece; each player can have indeed one frozen piece only per each
     * match. In particular, this string is composed of three characters: the first two characters represent the position of the cell
     * containing the frozen piece; the next is the number of remaining turns during which the piece will be frozen.
     */
    private String frozenPieceInformation;
    /**
     * This boolean is set to true as soon as a player's piece gets frozen. It is set to false as soon as it is unfrozen, that is,
     * when the piece has spent three turns in frozen state, or when it dies.
     */
	private boolean frozenPiece;
    /**
     * This attribute is described by a 4-character string, where each character is either the initial of the corresponding spell
     * ("F", "H", "R", or "T", to appear in this order) or a "0" if the spell was already used.
     */
    public String unusedSpells;
    /**
     * The total classic_score gained by this player in one or more matches.
     */
    public float score;


	/**
	 * Constructor.
	 */
    /**
     * The player object instantiated by this constructor is identified by his username; thus, he can be distinguishable for whatever match
     * he plays, and can collect scores as he plays them.
     * @param playerName the name of the player
     */
    public Player(String playerName) {
        name = playerName;
        frozenPieceInformation = "000";
        frozenPiece = false;
        unusedSpells = UNUSED_SPELLS;
        score = 0;
    }



    /**
	 * Getter, Setter methods.
	 */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

    public String getFrozenPieceInformation() {
        return frozenPieceInformation;
    }

    public void setFrozenPieceInformation(String frozenPieceInformation) {
        this.frozenPieceInformation = frozenPieceInformation;
    }

    public boolean hasFrozenPiece() {
		return frozenPiece;
	}

	public void setFrozenPiece(boolean isFrozenPiece) {
		this.frozenPiece = isFrozenPiece;
	}

    public String getUnusedSpells() {
        return unusedSpells;
    }

    public  void setUnusedSpells(String unusedSpells) {
        this.unusedSpells = unusedSpells;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}