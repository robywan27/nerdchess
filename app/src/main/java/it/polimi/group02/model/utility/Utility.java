package it.polimi.group02.model.utility;


/**
 * This class contains all the constant or static variable to be used in the game. The advantage of grouping these kind of variables in
 * a single place is that of having a single place to modify one value instead of locating anywhere in the code the value to be changed.
 */
public class Utility {
    public static final int NUMBER_OF_PLAYERS = 2;
    public static final int NUMBER_OF_PIECES_PER_PLAYER = 8;
    public static final int BOARD_SIDE_SIZE = 6;

    public static final String STANDARD_GAME_CONFIGURATION = "000000GK00saDS00kmMK00sdAS00kg000000";
    public static final String STANDARD_GAME_VITALITIES = "5675434334345765";
    public static final int[][] SPECIAL_CELLS_RECORD = {{0, 0}, {0, 3}, {5, 2}, {5, 5}};

    public static int giant_initial_vitality = 5;
    public static int dragon_initial_vitality = 6;
    public static int mage_initial_vitality = 7;
    public static int archer_initial_vitality = 5;
    public static int squire_initial_vitality = 3;
    public static int knight_initial_vitality = 4;

    // List of unused spells
    public static final String UNUSED_SPELLS = "FHRT";

    // useful for calculation of points assigned to a player when he kills an enemy's piece
    public static final int MAX_STRENGTH = 4;
}