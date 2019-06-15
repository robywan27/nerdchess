package it.polimi.group02.model.validator;

import it.polimi.group02.model.GameModel;
import it.polimi.group02.model.Piece;
import it.polimi.group02.model.Player;
import it.polimi.group02.model.piece.Knight;
import it.polimi.group02.model.utility.Color;

import static it.polimi.group02.model.utility.Utility.BOARD_SIDE_SIZE;
import static java.lang.Character.isUpperCase;

/**
 * This class implements the spellsValidator by verifying if all the rules that concern a revive
 * are satisfied by the player that is trying to invoke it
 */
public class ReviveValidator extends ActionValidator implements SpellsValidator {

    private Player playerPlaying;
    private String errorMessage;
    private char symbolToRevive;
    private char[][] initialConfiguration;
    private int turn;

    /**
     * The constructor sets the target to 0,0 since for the heal there is no need of a target
     *
     * @param cells         contains the game situation as matrix of chars
     * @param startingX     this is the x coordinate of the position from which we are trying to attack
     * @param startingY     this is the y coordinate of the position from which we are trying to attack
     * @param playerPlaying is used to know which player is playing, is set as an attribute of the class
     */
    public ReviveValidator(char[][] cells, Piece piece, int startingX, int startingY, Player playerPlaying, char[][] initialConfiguration, int turn) {
        super(cells, piece, startingX, startingY, 0, 0);
        this.playerPlaying = playerPlaying;
        this.initialConfiguration = initialConfiguration;
        this.turn = turn;
    }

    /**
     * This method verifies if all the rules concerning the teleport are not violated
     *
     * @return true if the spell can be done
     */
    public boolean checkSpell() {
        if (playerPlaying.unusedSpells.contains("R")) {
            if (initialConfiguration[getStartingX()][getStartingY()] == '0') {
                this.setErrorMessage("ERROR: revive can't be cast because in this position originally there was no piece");
                return false;
            } else {
                symbolToRevive = initialConfiguration[getStartingX()][getStartingY()];
                if (turn == 0 && !isUpperCase(symbolToRevive) || turn == 1 && isUpperCase(symbolToRevive)) {
                    this.setErrorMessage("ERROR: can't revive enemy's piece");
                    return false;
                } else if ((turn == 0 && isUpperCase(getCellSymbols()[getStartingX()][getStartingY()]) && getCellSymbols()[getStartingX()][getStartingY()] != '0') ||
                        (turn == 1 && !isUpperCase(getCellSymbols()[getStartingX()][getStartingY()]) && getCellSymbols()[getStartingX()][getStartingY()] != '0')) {
                    this.setErrorMessage("ERROR: on the position you are trying to revive there is already another of your pieces");
                    return false;
                } else {
                    if (initialConfiguration[getStartingX()][getStartingY()] == 'M' || initialConfiguration[getStartingX()][getStartingY()] == 'm') {
                        this.setErrorMessage("ERROR: you can't revive a mage");
                        return false;
                    } else {
                        if (searchNumberOfPiecesOfTheSameTypeOnTheBoard(symbolToRevive)) {
                            return true;
                        } else {
                            this.setErrorMessage("ERROR: all the pieces of the kind you're trying to resurrect are still alive");
                            return false;
                        }
                    }
                }
            }

        } else {
            this.setErrorMessage("ERROR: revive can't be cast because already used");
            return false;
        }
    }

    /**
     * This method is used when we try to revive a piece we have to check if all the pieces of that type are already alive or not
     *
     * @param symbol symbol used to create the new piece
     * @return true if exceeded the number
     */
    private boolean searchNumberOfPiecesOfTheSameTypeOnTheBoard(char symbol) {
        /*
        the idea is the following: perform a scan of the initial configuration of the fantasy_board and the current one, and increment two
        counters, each keeping track of the number of times the given symbol is encountered. If the counters are equals, it means the
        current configuration has all pieces of the kind you want to revive, which is incorrect.
         */
        int counter1 = 0;
        int counter2 = 0;
        for (int i = 0; i < BOARD_SIDE_SIZE; i++) {
            for (int j = 0; j < BOARD_SIDE_SIZE; j++) {
                if (getCellSymbols()[i][j] == symbol) {
                    counter1++;
                }
                if (initialConfiguration[i][j] == symbol) {
                    counter2++;
                }
            }
        }
        return counter1 < counter2;

    }

    public char getSymbolToRevive() {
        return symbolToRevive;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}


