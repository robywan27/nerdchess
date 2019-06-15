package it.polimi.group02.model.validator;


import it.polimi.group02.model.Piece;

/**
 * This class defines a template for the concrete implementations each corresponding to a specific type of action validator.
 * In this game, two basic actions are defined: movement and attack. Each of these actions shall be validated in order to distinguish the
 * legal actions according to predefined rules against non-legal ones.
 */
public abstract class ActionValidator {
    private char[][] cellSymbols;
    private Piece piece;
    private int startingX;
    private int startingY;
    private int targetX;
    private int targetY;


    /**
     * Constructor.
     * @param cells this variable contains a matrix with chars representing the fantasy_board status
     * @param piece this is the piece that is trying to attack
     * @param startingX this is the x coordinate of the position from which we are trying to attack
     * @param startingY this is the y coordinate of the position from which we are trying to attack
     * @param targetX this is the target x position on which we want to send our attack
     * @param targetY this is the target y position on which we want to send our attack
     */
    public ActionValidator(char[][] cells, Piece piece, int startingX, int startingY, int targetX, int targetY) {
        this.cellSymbols = cells;
        this.piece = piece;
        this.startingX = startingX;
        this.startingY = startingY;
        this.targetX = targetX;
        this.targetY = targetY;
    }


    /**
     * Getters, setters.
     */
    public char[][] getCellSymbols() {
        return cellSymbols;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getStartingX() {
        return startingX;
    }

    public int getStartingY() {
        return startingY;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }
}
