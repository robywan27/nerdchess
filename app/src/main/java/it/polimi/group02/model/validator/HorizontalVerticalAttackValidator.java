package it.polimi.group02.model.validator;


import java.util.ArrayList;
import java.util.List;

import it.polimi.group02.model.Piece;
import it.polimi.group02.model.piece.Mage;
import it.polimi.group02.model.piece.Squire;
import it.polimi.group02.model.utility.Status;

import static it.polimi.group02.model.utility.Utility.BOARD_SIDE_SIZE;
import static java.lang.Character.isLowerCase;

/**
 * This class, as for the move validator, is used to check if the piece that is trying to attack is not violating any rule;
 * so it checks if you are not attacking your player and even if there no other players in the attacking direction
 */
public class HorizontalVerticalAttackValidator extends ActionValidator implements AttackValidator {
    private static ArrayList<String> enemyCells;
    private String errorMessage;

    /**
     * The constructor recalls the super class constructor
     * @param cells this variable contains a matrix with chars representing the fantasy_board status
     * @param piece this is the piece that is trying to attack
     * @param startingX this is the x coordinate of the position from which we are trying to attack
     * @param startingY this is the y coordinate of the position from which we are trying to attack
     * @param targetX this is the target x position on which we want to send our attack
     * @param targetY this is the target y position on which we want to send our attack
     */
    public HorizontalVerticalAttackValidator(char[][] cells, Piece piece, int startingX, int startingY, int targetX, int targetY) {
        super(cells, piece, startingX, startingY, targetX, targetY);
    }

    /**
     * This method is used to check if the attack is feasible or not. It considers the 4 directions that can be reached by moving
     * horizontally and vertically. Each flag represents if in that path direction there is or not another piece, even if player's playing
     * or opponent's one, the path must be empty until the target is reached. If the flag is true it means that we can attack in that direction
     * @return true if it is possible to attack in the selected destination
     */
    public boolean checkAttack() {
        // check if target is not an ally
        boolean bothUpperCase = Character.isUpperCase(getCellSymbols()[getTargetX()][getTargetY()]) && Character.isUpperCase(getCellSymbols()[getStartingX()][getStartingY()]);
        boolean bothLowerCase = isLowerCase(getCellSymbols()[getTargetX()][getTargetY()]) && isLowerCase(getCellSymbols()[getStartingX()][getStartingY()]);
        if (bothUpperCase || bothLowerCase) {
            errorMessage = "ERROR: you can't attack an ally";
            return false;
        }

        boolean northFlag = false;
        boolean southFlag = false;
        boolean eastFlag = false;
        boolean westFlag = false;

        int startX = this.getStartingX();
        int startY = this.getStartingY();
        int targetX = this.getTargetX();
        int targetY = this.getTargetY();

        enemyCells = new ArrayList<>();

        for (int i = 1; i <= getPiece().getAttackRange().getValue(); i++) {
            // check north direction
            //  1. check if new position is out of fantasy_board or if in the new cell there's an ally piece
            if ((startX - i < 0) || belongToSamePlayer(startX - i, startY)) {
                northFlag = true;
            }
            // if you find an enemy, you add the cell to the list of cells with enemies
            else if (!northFlag && isEnemy(startX - i, startY)) {
                enemyCells.add(String.valueOf(startX - i) + String.valueOf(startY) + ";");
                northFlag = true;
            }

            // check south direction
            if ((startX + i >= BOARD_SIDE_SIZE) || belongToSamePlayer(startX + i, startY)) {
                southFlag = true;
            } else if (!southFlag && isEnemy(startX + i, startY)) {
                enemyCells.add(String.valueOf(startX + i) + String.valueOf(startY) + ";");
                southFlag = true;
            }

            // check east direction
            if ((startY - i < 0) || belongToSamePlayer(startX, startY - i)) {
                eastFlag = true;
            } else if (!eastFlag && isEnemy(startX, startY - i)) {
                enemyCells.add(String.valueOf(startX) + String.valueOf(startY - i) + ";");
                eastFlag = true;
            }

            // check west direction
            if ((startY + i >= BOARD_SIDE_SIZE) || belongToSamePlayer(startX, startY + i)) {
                westFlag = true;
            } else if (!westFlag && isEnemy(startX, startY + i)) {
                enemyCells.add(String.valueOf(startX) + String.valueOf(startY + i) + ";");
                westFlag = true;
            }
        }

        for (int i = 0; i < enemyCells.size(); i++) {
            if (String.valueOf(enemyCells.get(i).charAt(0)).equals(String.valueOf(targetX)) &&
                    String.valueOf(enemyCells.get(i).charAt(1)).equals(String.valueOf(targetY))) {
                return true;
            }
        }

        errorMessage = "ERROR: it's not possible to make an attack";
        return false;
    }


    /**
     * This method has been designed to find the positions of the enemies within the attack range of a piece without any given target.
     * It's been defined as static so it can be called without the need to instantiate this class.
     * @param piece the starting piece
     * @param gameBoard the current configuration of the fantasy_board
     * @return a list of strings, each representing a cell position
     */
    public static ArrayList<String> checkAttackWithoutTarget(Piece piece, char[][] gameBoard) {
        boolean northFlag = false;
        boolean southFlag = false;
        boolean eastFlag = false;
        boolean westFlag = false;

        int startX = piece.getxCoordinate();
        int startY = piece.getyCoordinate();

        enemyCells = new ArrayList<>();

        for (int i = 1; i <= piece.getAttackRange().getValue(); i++) {
            // check north direction
            //  1. check if new position is out of fantasy_board or if in the new cell there's an ally piece
            if ((startX - i < 0) || (Character.isUpperCase(gameBoard[startX - i][startY]) && Character.isUpperCase(piece.getPieceSymbol())
                                 || Character.isLowerCase(gameBoard[startX - i][startY]) && Character.isLowerCase(piece.getPieceSymbol()))) {

                northFlag = true;
            }
            // if you find an enemy, you add the cell to the list of cells with enemies
            else if (!northFlag && (Character.isLowerCase(gameBoard[startX - i][startY]) && Character.isUpperCase(piece.getPieceSymbol())
                                || Character.isUpperCase(gameBoard[startX - i][startY]) && Character.isLowerCase(piece.getPieceSymbol()))) {

                enemyCells.add(String.valueOf(startX - i) + String.valueOf(startY) + ";");
                northFlag = true;
            }

            // check south direction
            if ((startX + i >= BOARD_SIDE_SIZE) || (Character.isUpperCase(gameBoard[startX + i][startY]) && Character.isUpperCase(piece.getPieceSymbol())
                    || Character.isLowerCase(gameBoard[startX + i][startY]) && Character.isLowerCase(piece.getPieceSymbol()))) {

                southFlag = true;
            } else if (!southFlag && (Character.isLowerCase(gameBoard[startX + i][startY]) && Character.isUpperCase(piece.getPieceSymbol())
                    || Character.isUpperCase(gameBoard[startX + i][startY]) && Character.isLowerCase(piece.getPieceSymbol()))) {

                enemyCells.add(String.valueOf(startX + i) + String.valueOf(startY) + ";");
                southFlag = true;
            }

            // check east direction
            if ((startY - i < 0) || (Character.isUpperCase(gameBoard[startX][startY - i]) && Character.isUpperCase(piece.getPieceSymbol())
                    || Character.isLowerCase(gameBoard[startX][startY - i]) && Character.isLowerCase(piece.getPieceSymbol()))) {
                eastFlag = true;
            } else if (!eastFlag && (Character.isLowerCase(gameBoard[startX][startY - i]) && Character.isUpperCase(piece.getPieceSymbol())
                    || Character.isUpperCase(gameBoard[startX][startY - i]) && Character.isLowerCase(piece.getPieceSymbol()))) {
                enemyCells.add(String.valueOf(startX) + String.valueOf(startY - i) + ";");
                eastFlag = true;
            }

            // check west direction
            if ((startY + i >= BOARD_SIDE_SIZE) || (Character.isUpperCase(gameBoard[startX][startY + i]) && Character.isUpperCase(piece.getPieceSymbol())
                    || Character.isLowerCase(gameBoard[startX][startY + i]) && Character.isLowerCase(piece.getPieceSymbol()))) {
                westFlag = true;
            } else if (!westFlag && (Character.isLowerCase(gameBoard[startX][startY + i]) && Character.isUpperCase(piece.getPieceSymbol())
                    || Character.isUpperCase(gameBoard[startX][startY + i]) && Character.isLowerCase(piece.getPieceSymbol()))) {
                enemyCells.add(String.valueOf(startX) + String.valueOf(startY + i) + ";");
                westFlag = true;
            }
        }

        return enemyCells;
    }


    /**
     * This method is used to know if the piece that is trying to attack and the destination piece are from the same player. If they
     * are from the same player it means that it can't be attacked
     * @param targetX represents the x coordinate of the destination piece that we want to attack
     * @param targetY represents the y coordinate of the destination piece that we want to attack
     * @return true if the pieces belong to the same player
     */
    public boolean belongToSamePlayer(int targetX, int targetY) {
        boolean bothUpperCase = Character.isUpperCase(this.getCellSymbols()[targetX][targetY]) && Character.isUpperCase(this.getPiece().getPieceSymbol());
        boolean bothLowerCase = Character.isLowerCase(this.getCellSymbols()[targetX][targetY]) && Character.isLowerCase(this.getPiece().getPieceSymbol());

        return (bothUpperCase || bothLowerCase);
    }

    /**
     * This method is used to know if the piece that is trying to attack and the destination piece are from different players. If they
     * are from the different players it means that it can be attacked
     * @param targetX represents the x coordinate of the destination piece that we want to attack
     * @param targetY represents the y coordinate of the destination piece that we want to attack
     * @return true if the pieces belong to different players
     */
    public boolean isEnemy(int targetX, int targetY) {
        boolean upperVsLowerCase = Character.isLowerCase(this.getCellSymbols()[targetX][targetY]) && Character.isUpperCase(this.getPiece().getPieceSymbol());
        boolean lowerVsUpperCase = Character.isUpperCase(this.getCellSymbols()[targetX][targetY]) && Character.isLowerCase(this.getPiece().getPieceSymbol());

        return (upperVsLowerCase || lowerVsUpperCase);
    }


    public String getErrorMessage() {
        return errorMessage;
    }
}
