package it.polimi.group02.model;

import it.polimi.group02.model.GameModel;
import it.polimi.group02.model.utility.Utility;

import static it.polimi.group02.model.utility.Color.BLACK;
import static it.polimi.group02.model.utility.Color.WHITE;


/**
 * This class sets a game according to a string representing any game configuration, which can differ from the standard initial one.
 */
public class StringToGameConverter {

    /**
     * GameModel instance to set according to the input string
     */
    private GameModel gameModel;

    /**
     * Constructor
     */
    public StringToGameConverter() {
        gameModel = new GameModel();
        gameModel.initializePlayer("", 0, WHITE);
        gameModel.initializePlayer("", 1, BLACK);
    }

    public StringToGameConverter(String whitePlayerName, String blackPlayerName) {
        gameModel = new GameModel();
        gameModel.initializePlayer(whitePlayerName, 0, WHITE);
        gameModel.initializePlayer(blackPlayerName, 1, BLACK);
    }


    /**
     * This method is a sort of a controller of the turns that leads the game to it's end configuration
     * The input string (the state + the turn(s)) of 72+ characters has the following format:
     *   – The first character represents the moving player ("B" for black, "W" for white)
     *   – The next 36 characters represent the fantasy_board
     *   – The next 16 characters represent the vitality
     *   – The next 6 characters represent the frozen pieces
     *   – The next 8 characters represent the unused spells
     *   – The next (5 or more) characters represent a sequence of turns
     * @param gameConfiguration a string representing any game configuration
     * @return string representing the outcome of the input string
     */
    public String turnTest(String gameConfiguration){
        //the following variables are used to extrapolate the information from the initial configuration
        int firstCharOfBoardConfiguration = 1;
        int numberOfCharsIndicatingTheFrozenPieces = 6;
        int lastCharOfBoardConfiguration = firstCharOfBoardConfiguration + Utility.BOARD_SIDE_SIZE*Utility.BOARD_SIDE_SIZE;
        int configurationWithoutMoves = lastCharOfBoardConfiguration + Utility.NUMBER_OF_PIECES_PER_PLAYER*2 +
                numberOfCharsIndicatingTheFrozenPieces + Utility.UNUSED_SPELLS.length()*2;

        if(gameConfiguration.substring(firstCharOfBoardConfiguration,lastCharOfBoardConfiguration).equals(Utility.STANDARD_GAME_CONFIGURATION)) {
            gameModel.createStandardConfigurationMatrix();
        }
        else {
            gameModel.createCustomConfigurationMatrix(gameConfiguration.substring(firstCharOfBoardConfiguration,lastCharOfBoardConfiguration));
        }

        //setting who is starting the next turn
        if(gameConfiguration.substring(0,1).equals("W")) {
            gameModel.setTurn(0);
        }else{
            gameModel.setTurn(1);
        }

        //initializing the pieces
        gameModel.createPieces();

        //setting custom vitalities (can be the same as the standard one)
        if (gameModel.setVitalities(gameConfiguration.substring(lastCharOfBoardConfiguration,
                lastCharOfBoardConfiguration + Utility.NUMBER_OF_PIECES_PER_PLAYER*2)).length() > 0)
            return gameModel.setVitalities(gameConfiguration.substring(lastCharOfBoardConfiguration,
                    lastCharOfBoardConfiguration + Utility.NUMBER_OF_PIECES_PER_PLAYER*2));
        // setting number of dead pieces variable to number of pieces already dead
        int numberOfDeadPieces = 0;
        for (int i = 0; i < gameConfiguration.substring(lastCharOfBoardConfiguration, lastCharOfBoardConfiguration + Utility.NUMBER_OF_PIECES_PER_PLAYER*2).length(); i++) {
            if (gameConfiguration.substring(lastCharOfBoardConfiguration, lastCharOfBoardConfiguration + Utility.NUMBER_OF_PIECES_PER_PLAYER*2).charAt(i) == '0') {
                numberOfDeadPieces++;
            }
        }
        gameModel.setNumberOfDeadPieces(numberOfDeadPieces);

        //setting the frozen pieces for the player1
        gameModel.setPlayerFrozenPieces(0, gameConfiguration.substring(lastCharOfBoardConfiguration + Utility.NUMBER_OF_PIECES_PER_PLAYER*2,
                lastCharOfBoardConfiguration + Utility.NUMBER_OF_PIECES_PER_PLAYER*2 + numberOfCharsIndicatingTheFrozenPieces/2));

        //setting the frozen pieces for the player2
        gameModel.setPlayerFrozenPieces(1, gameConfiguration.substring(lastCharOfBoardConfiguration + Utility.NUMBER_OF_PIECES_PER_PLAYER*2 + numberOfCharsIndicatingTheFrozenPieces/2,
                lastCharOfBoardConfiguration + Utility.NUMBER_OF_PIECES_PER_PLAYER*2 + numberOfCharsIndicatingTheFrozenPieces));

        //setting the unused spell for player 1
        gameModel.setPlayerUnusedSpells(0, gameConfiguration.substring(lastCharOfBoardConfiguration + Utility.NUMBER_OF_PIECES_PER_PLAYER*2 + numberOfCharsIndicatingTheFrozenPieces,
                lastCharOfBoardConfiguration + Utility.NUMBER_OF_PIECES_PER_PLAYER*2 + numberOfCharsIndicatingTheFrozenPieces + Utility.UNUSED_SPELLS.length()));

        //setting the unused spell for player 2
        gameModel.setPlayerUnusedSpells(1, gameConfiguration.substring(lastCharOfBoardConfiguration + Utility.NUMBER_OF_PIECES_PER_PLAYER*2 + numberOfCharsIndicatingTheFrozenPieces + Utility.UNUSED_SPELLS.length(),
                lastCharOfBoardConfiguration + Utility.NUMBER_OF_PIECES_PER_PLAYER*2 + numberOfCharsIndicatingTheFrozenPieces + Utility.UNUSED_SPELLS.length()*2));

        //67 is the number of chars in the configuration that precede the actions
        if( (gameConfiguration.length()-configurationWithoutMoves)%5 == 0) {//if the condition is true it means that the length of the initial configuration is correct
            for (int i = 0; i < (gameConfiguration.length() - configurationWithoutMoves); i += 5) {
                // if either an error occurs or the game has ended
                if (gameModel.playTurn(gameConfiguration.substring(configurationWithoutMoves + i, configurationWithoutMoves + 5 + i)).length() > 0) {
                    break;
                }
            }
        }else{
            return "ERROR: wrong game configuration, missing or exceeding some final characters";
        }

        return gameModel.toString();
    }


    public GameModel getGameModel() {
        return gameModel;
    }
}