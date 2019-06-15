package it.polimi.group02.controller.database;


import java.util.List;

import it.polimi.group02.model.GameModel;
import it.polimi.group02.model.statistics.GameStatistics;
import it.polimi.group02.model.statistics.PlayerStatistics;

/**
 * This interface has a bridge role between the Android (Java) classes and the relational database; in fact, this interface
 * demands to the classes implementing it to provide a body its methods, that either allow to convert objects in relational entities
 * or vice versa to convert relational entities in objects.
 */
public interface StatisticsDatabase {
    /**
     * This method allows to store some information about a player in the database.
     * @param playerName the name of the player
     * @param points the points assigned to the player in a game
     */
    void savePlayer(String playerName, int points, int matchOutcome);

    /**
     * This method allows to save some information about a game in the database.
     * @param game an instance of the game
     */
    void saveGame(GameModel game);

    /**
     * This method allows to retrieve from the database a list of players ordered by a decreasing number of points.
     * @return
     */
    List<PlayerStatistics> retrieveBestPlayersStatistics();

    /**
     * This method allows to retrieve from the database a list of the games ordered by a decreasing date, that is, from most recent to oldest.
     * @return
     */
    List<GameStatistics> retrieveLatestGames();

}
