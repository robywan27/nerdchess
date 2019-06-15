package it.polimi.group02.model.statistics;


import java.util.Date;

/**
 * This class stores all the information needed to collect statistics regarding each game.
 */
public class GameStatistics {
    /**
     * This attribute stores the formatted string with the date on which this instance of game is created.
     */
    private String dateFormat;
    /**
     * This attribute stores the name of the white player in this game.
     */
    private String whitePlayer;
    /**
     * This attribute stores the name of the white player in this game.
     */
    private String blackPlayer;
    /**
     * This attribute stores the name of the winner of this game.
     */
    private String winner;
    /**
     * This attribute stores the number of pieces at the end of this game.
     */
    private int numberOfPiecesLeft;
    /**
     * This attribute stores the number of rounds played in this game.
     */
    private int numberOfTurns;
    /**
     * This attribute is set to "Yes" if the winner has won by occupying three or more special cells, "No" otherwise (he killed all
     * opponent's pieces).
     */
    private String occupiedThreeSpecialCells;



    /**
     * Getters, setters
     */

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public String getWinner() {
        return winner;
    }

    public int getNumberOfPiecesLeft() {
        return numberOfPiecesLeft;
    }

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public String getOccupiedThreeSpecialCells() {
        return occupiedThreeSpecialCells;
    }

    public void setWhitePlayer(String whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public void setBlackPlayer(String blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setNumberOfPiecesLeft(int numberOfPiecesLeft) {
        this.numberOfPiecesLeft = numberOfPiecesLeft;
    }

    public void setNumberOfTurns(int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public void setOccupiedThreeSpecialCells(String occupiedThreeSpecialCells) {
        this.occupiedThreeSpecialCells = occupiedThreeSpecialCells;
    }
}
