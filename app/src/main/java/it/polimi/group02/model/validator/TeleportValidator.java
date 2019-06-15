
package it.polimi.group02.model.validator;

import it.polimi.group02.model.GameModel;
import it.polimi.group02.model.Piece;
import it.polimi.group02.model.Player;
import it.polimi.group02.model.utility.Color;

import static java.lang.Character.isUpperCase;

/**
 * This class implements the spellsValidator by verifying if all the rules that concern a teleport
 * are satisfied by the player that is trying to invoke it
 */
public class TeleportValidator extends ActionValidator implements SpellsValidator {

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
    public TeleportValidator(char[][] cells, Piece piece, int startingX, int startingY, int xTarget, int yTarget, Player playerPlaying) {
        super(cells, piece, startingX, startingY, xTarget, yTarget);
        this.playerPlaying = playerPlaying;
        this.selectedPiece = piece;
    }

    /**
     * This method verifies if all the rules concerning the teleport are not violated
     * @return true if the spell can be done
     */
    public boolean checkSpell(){
        // check to see if the piece has available spells
        if (!playerPlaying.unusedSpells.contains("T")) {
            errorMessage = "ERROR: this player can teleport no more";
            return false;
        }
        if ((selectedPiece.getPieceSymbol() == 'M' && isUpperCase(selectedPiece.getPieceSymbol())) ||
                selectedPiece.getPieceSymbol() == 'm' && !isUpperCase(selectedPiece.getPieceSymbol())) {
            errorMessage = "ERROR: you can't teleport a mage";
            return false;
        }
        if (getCellSymbols()[getTargetX()][getTargetY()] == 'M' || getCellSymbols()[getTargetX()][getTargetY()] == 'm') {
            errorMessage = "ERROR: you can't teleport a piece on a cell occupied by a mage";
            return false;
        }
        if ((isUpperCase(selectedPiece.getPieceSymbol()) && isUpperCase(getCellSymbols()[getTargetX()][getTargetY()]) && getCellSymbols()[getTargetX()][getTargetY()] != '0') ||
                (!isUpperCase(selectedPiece.getPieceSymbol()) && !isUpperCase(getCellSymbols()[getTargetX()][getTargetY()]) && getCellSymbols()[getTargetX()][getTargetY()] != '0')) {
            errorMessage = "ERROR: you can't teleport a piece on a cell occupied by an allied piece";
            return false;
        }
        if (GameModel.isSpecialCell(getStartingX(), getStartingY())) {
            errorMessage = "ERROR: you can't teleport a piece standing on a special cell";
            return false;
        }
        if (GameModel.isSpecialCell(getTargetX(), getTargetY())) {
            errorMessage = "ERROR: you can't teleport a piece to a special cell";
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
