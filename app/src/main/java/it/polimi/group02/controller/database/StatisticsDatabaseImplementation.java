package it.polimi.group02.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.polimi.group02.model.GameModel;
import it.polimi.group02.model.statistics.GameStatistics;
import it.polimi.group02.model.statistics.PlayerStatistics;



/**
 * This class implements the interface GameStatisticsDatabase, and thus implements its methods. It holds an instance of the database
 * in which to store all the relevant information regarding the games and players.
 */
public class StatisticsDatabaseImplementation extends SQLiteOpenHelper implements StatisticsDatabase {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "nerdChess.db";

    private static final String GAME_STATISTICS = "gameStatistics";
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String WHITE_PLAYER = "whitePlayer";
    private static final String BLACK_PLAYER = "blackPlayer";
    private static final String WINNER = "winner";
    private static final String NUMBER_OF_PIECES_LEFT = "numberOfPiecesLeft";
    private static final String NUMBER_OF_TURNS = "numberOfTurns";
    private static final String OCCUPIED_THREE_SPECIAL_CELLS = "occupiedThreeSpecialCells";

    private static final String PLAYER_STATISTICS = "playerStatistics";
    private static final String NAME = "name";
    private static final String SCORE = "classic_score";
    private static final String NUMBER_OF_GAMES_PLAYED = "numberOfGamesPlayed";
    private static final String NUMBER_OF_VICTORIES = "numberOfVictories";
    private static final String NUMBER_OF_LOSSES = "numberOfLosses";

    // create game history table
    private static final String GAME_TABLE_CREATION = "CREATE TABLE " + GAME_STATISTICS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DATE + " TEXT, " + WHITE_PLAYER + " TEXT, " + BLACK_PLAYER + " TEXT, " + WINNER + " TEXT, " + NUMBER_OF_PIECES_LEFT +
            " INTEGER, " + NUMBER_OF_TURNS + " INTEGER, " + OCCUPIED_THREE_SPECIAL_CELLS + " TEXT)";

    // create player table
    private static final String PLAYER_TABLE_CREATION = "CREATE TABLE " + PLAYER_STATISTICS + "(" + NAME + " TEXT PRIMARY KEY, " + SCORE + " INTEGER, " +
            NUMBER_OF_GAMES_PLAYED + " INTEGER, " + NUMBER_OF_VICTORIES + " TEXT, " + NUMBER_OF_LOSSES + " TEXT)";

    // drop history table on update
    private static final String GAME_TABLE_DESTRUCTION = "DROP TABLE IF EXISTS " + GAME_STATISTICS;
    // drop player table on update
    private static final String PLAYER_TABLE_DESTRUCTION = "DROP TABLE IF EXISTS " + PLAYER_STATISTICS;

    // query to select the needed elements to calculate a player's score and other statistics
    private static final String QUERY_PLAYER_STATISTICS = "SELECT " + NUMBER_OF_GAMES_PLAYED + ", " + SCORE + ", " + NUMBER_OF_VICTORIES +
            ", " + NUMBER_OF_LOSSES + " FROM " + PLAYER_STATISTICS + " WHERE " + NAME + " = ? ";

    // query to retrieve the last k games by most recent date
    private static final String RETRIEVE_GAME_STATISTICS_QUERY = "SELECT * FROM " + GAME_STATISTICS + " ORDER BY " + DATE + " DESC LIMIT 25";
    // query to retrieve the last k players ranked by highest score
    private static final String RETRIEVE_PLAYER_STATISTICS_QUERY = "SELECT * FROM " + PLAYER_STATISTICS + " ORDER BY " + SCORE + " DESC LIMIT 10";


    /**
     * Constructor
     */
    public StatisticsDatabaseImplementation(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GAME_TABLE_CREATION);
        db.execSQL(PLAYER_TABLE_CREATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(GAME_TABLE_DESTRUCTION);
        db.execSQL(PLAYER_TABLE_DESTRUCTION);

        onCreate(db);
    }

    @Override
    public void saveGame(GameModel game) {
        // get the data repository in write mode
        SQLiteDatabase database = this.getWritableDatabase();

        // create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DATE, game.getDateFormat());
        values.put(WHITE_PLAYER, game.getWhitePlayerName());
        values.put(BLACK_PLAYER, game.getBlackPlayerName());
        values.put(WINNER, game.getWinner());
        values.put(NUMBER_OF_PIECES_LEFT, game.getNumberOfPiecesLeft());
        values.put(NUMBER_OF_TURNS, game.getNumberOfTurns());
        String result = game.hasOccupiedThreeSpecialCells() ? "yes" : "no";
        values.put(OCCUPIED_THREE_SPECIAL_CELLS, result);

        database.insert(GAME_STATISTICS, null, values);
        database.close();
    }

    @Override
    public void savePlayer(String playerName, int score, int matchOutcome) {
        SQLiteDatabase database = this.getWritableDatabase();
        Log.i("test", "Saving game");

        ContentValues values = new ContentValues();
        values.put(NAME, playerName);

        int numberOfGames = 1;
        int numberOfVictories = matchOutcome;
        int numberOfLosses = (matchOutcome + 1) % 2;
        boolean playerInDatabase = false;

        // calculate the score for this player and update the other statistics
        Cursor cursor = database.rawQuery(QUERY_PLAYER_STATISTICS, new String[] {playerName});
        if (cursor.moveToFirst()) {
            do {
                numberOfGames += cursor.getInt(0);
                score += cursor.getInt(1);
                numberOfVictories += cursor.getInt(2);
                numberOfLosses += cursor.getInt(3);
                playerInDatabase = true;
            } while (cursor.moveToNext());
        }
        cursor.close();

        values.put(SCORE, score);
        values.put(NUMBER_OF_GAMES_PLAYED, numberOfGames);
        values.put(NUMBER_OF_VICTORIES, numberOfVictories);
        values.put(NUMBER_OF_LOSSES, numberOfLosses);

        if (playerInDatabase) {
            database.update(PLAYER_STATISTICS, values, NAME + " = ? ", new String[] {playerName});
        }
        else {
            database.insert(PLAYER_STATISTICS, null, values);
        }

        database.close();
        Log.i("test", "Game saved");
    }

    @Override
    public List<GameStatistics> retrieveLatestGames() {
        List<GameStatistics> gameStatistics = new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(RETRIEVE_GAME_STATISTICS_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                GameStatistics gs = new GameStatistics();

                gs.setDateFormat(cursor.getString(1));
                gs.setWhitePlayer(cursor.getString(2));
                gs.setBlackPlayer(cursor.getString(3));
                gs.setWinner(cursor.getString(4));
                gs.setNumberOfPiecesLeft(cursor.getInt(5));
                gs.setNumberOfTurns(cursor.getInt(6));
                gs.setOccupiedThreeSpecialCells(cursor.getString(7));

                gameStatistics.add(gs);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return gameStatistics;
    }

    @Override
    public List<PlayerStatistics> retrieveBestPlayersStatistics() {
        List<PlayerStatistics> playerStatistics = new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(RETRIEVE_PLAYER_STATISTICS_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                PlayerStatistics ps = new PlayerStatistics();

                ps.setName(cursor.getString(0));
                ps.setScore(cursor.getInt(1));
                ps.setNumberOfGamesPlayed(cursor.getInt(2));
                ps.setNumberOfVictories(cursor.getInt(3));
                ps.setNumberOfLosses(cursor.getInt(4));

                playerStatistics.add(ps);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return playerStatistics;
    }
}
