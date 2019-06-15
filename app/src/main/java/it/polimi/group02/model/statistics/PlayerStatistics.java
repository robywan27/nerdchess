package it.polimi.group02.model.statistics;


/**
 * This class stores all the information needed to collect statistics regarding each player.
 */
public class PlayerStatistics {
    /**
     * This attribute stores the name of this player.
     */
    private String name;
    /**
     * This attribute stores the total classic_score accumulated by the player in all the games played by him.
     */
    private int score;
    /**
     * This attribute stores the total number of games played by this player.
     */
    private int numberOfGamesPlayed;
    /**
     * This attribute stores the total number of victories won by this player.
     */
    private int numberOfVictories;
    /**
     * This attribute stores the total number of losses lost by this player.
     */
    private int numberOfLosses;



    /**
     * Getters, setters
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumberOfGamesPlayed() {
        return numberOfGamesPlayed;
    }

    public void setNumberOfGamesPlayed(int numberOfGamesPlayed) {
        this.numberOfGamesPlayed = numberOfGamesPlayed;
    }

    public int getNumberOfVictories() {
        return numberOfVictories;
    }

    public void setNumberOfVictories(int numberOfVictories) {
        this.numberOfVictories = numberOfVictories;
    }

    public int getNumberOfLosses() {
        return numberOfLosses;
    }

    public void setNumberOfLosses(int numberOfLosses) {
        this.numberOfLosses = numberOfLosses;
    }
}
