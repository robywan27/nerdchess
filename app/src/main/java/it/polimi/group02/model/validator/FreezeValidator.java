package it.polimi.group02.model.validator;

import it.polimi.group02.model.GameModel;
import it.polimi.group02.model.Piece;
import it.polimi.group02.model.Player;
import it.polimi.group02.model.utility.Color;

import static java.lang.Character.isUpperCase;

/**
 * This class implements the spellsValidator by verifying if all the rules that concern an heal
 * are satisfied by the player that is trying to invoke it
 */
public class FreezeValidator extends ActionValidator implements SpellsValidator {

    private Player playerPlaying;
    private Piece selectedPiece;
    private int turn;
    private String errorMessage;

    /**
     * The constructor sets the target to 0,0 since for the heal there is no need of a target
     * @param cells contains the game situation as matrix of chars
     * @param piece is the piece to be frozen
     * @param startingX this is the x coordinate of the position from which we are trying to attack
     * @param startingY this is the y coordinate of the position from which we are trying to attack
     * @param playerPlaying is used to know which player is playing, is set as an attribute of the class
     * @param turn used to know whick player is playing
     */
    public FreezeValidator(char[][] cells, Piece piece, int startingX, int startingY, Player playerPlaying, int turn) {
        super(cells, piece, startingX, startingY, 0, 0);
        this.playerPlaying = playerPlaying;
        this.selectedPiece = piece;
        this.turn = turn;
    }

    /**
     * This method verifies if all the rules concerning the freeze are not violated
     * @return true if the spell can be done
     */
    public boolean checkSpell(){
        if(playerPlaying.getUnusedSpells().contains("F")) {
            if (selectedPiece.getPieceSymbol() == 'm' || selectedPiece.getPieceSymbol() == 'M') {
                this.errorMessage = "ERROR: You can't freeze a mage";
                return false;
            } else if (GameModel.isSpecialCell(getStartingX(),getStartingY())) {
                this.errorMessage = "ERROR: Can't cast spells on special cells";
                return false;
            }
            return true;
        }else{
            this.errorMessage = "ERROR: This player cannot cast spells";
            return false;
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
