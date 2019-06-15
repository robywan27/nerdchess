package it.polimi.group02.model.validator;

import it.polimi.group02.model.GameModel;
import it.polimi.group02.model.Piece;
import it.polimi.group02.model.Player;

import static java.lang.Character.isUpperCase;

/**
 * This class implements the spellsValidator by verifying if all the rules that concern an heal
 * are satisfied by the player that is trying to invoke it
 */
public class HealValidator extends ActionValidator implements SpellsValidator {

    private Player playerPlaying;
    private Piece selectedPiece;
    private String errorMessage;

    /**
     * The constructor sets the target to 0,0 since for the heal there is no need of a target
     * @param cells contains the game situation as matrix of chars
     * @param piece is the piece that is trying to move
     * @param startingX this is the x coordinate of the position from which we are trying to attack
     * @param startingY this is the y coordinate of the position from which we are trying to attack
     * @param playerPlaying is used to know which player is playing, is set as an attribute of the class
     */
    public HealValidator(char[][] cells, Piece piece, int startingX, int startingY, Player playerPlaying) {
        super(cells, piece, startingX, startingY, 0, 0);
        this.playerPlaying = playerPlaying;
        this.selectedPiece = piece;
    }

    /**
     * This method verifies if all the rules concerning the heal are not violated
     * @return true if the spell can be done
     */
    public boolean checkSpell(){
        // check to see if the moving piece is the mage
        if (!playerPlaying.unusedSpells.contains("H")) {
            errorMessage = "ERROR: the selected player can heal no more";
            return false;
        }
        if (GameModel.isSpecialCell(getStartingX(), getStartingY())) {
            errorMessage = "ERROR: you can't heal a piece standing on a special cell";
            return false;
        }
        if ((selectedPiece.getPieceSymbol() == 'M' && isUpperCase(selectedPiece.getPieceSymbol())) ||
                selectedPiece.getPieceSymbol() == 'm' && !isUpperCase(selectedPiece.getPieceSymbol())) {
            errorMessage = "ERROR: you can't heal a mage";
            return false;
        }
        else {
            return true;
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
