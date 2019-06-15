package it.polimi.group02.model;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.group02.model.utility.Utility.NUMBER_OF_PLAYERS;

/**
 * This class it's used to copy the attributes of a game status to another
 */
public class CopyGameStatus {
    public static GameModel copyGameStatus(GameModel actualGameStatus) {
        GameModel newGame = new GameModel();
        char[][] tempConfig = new char[6][6];
        for(int i = 0; i< 6; i++){
            for(int j = 0; j< 6; j++){
                tempConfig[i][j] = actualGameStatus.getCurrentConfiguration()[i][j];
            }
        }
        newGame.setCurrentConfiguration(tempConfig);
        newGame.setTurn(actualGameStatus.getTurn());
        newGame.setNumberOfDeadPieces(actualGameStatus.getNumberOfDeadPieces());
        //players
        newGame.setPlayerOne(new Player(actualGameStatus.getPlayers()[0].getName()));
        newGame.setPlayerTwo(new Player(actualGameStatus.getPlayers()[1].getName()));
        newGame.getPlayers()[0].setFrozenPiece(actualGameStatus.getPlayers()[0].hasFrozenPiece());
        newGame.getPlayers()[1].setFrozenPiece(actualGameStatus.getPlayers()[1].hasFrozenPiece());
        newGame.getPlayers()[0].setFrozenPieceInformation(actualGameStatus.getPlayers()[0].getFrozenPieceInformation());
        newGame.getPlayers()[1].setFrozenPieceInformation(actualGameStatus.getPlayers()[1].getFrozenPieceInformation());
        newGame.getPlayers()[0].setColor(actualGameStatus.getPlayers()[0].getColor());
        newGame.getPlayers()[1].setColor(actualGameStatus.getPlayers()[1].getColor());
        newGame.setPlayerUnusedSpells(0, actualGameStatus.getPlayers()[0].getUnusedSpells());
        newGame.setPlayerUnusedSpells(1, actualGameStatus.getPlayers()[1].getUnusedSpells());
        //pieces
        newGame.createPieces();
        List<Piece>[] pieces = new List[NUMBER_OF_PLAYERS];
        pieces[0] = new ArrayList<>();
        pieces[1] = new ArrayList<>();
        int tempX;
        int tempY;
        char tempSymbol;
        Piece tempPiece;
        for(int k = 0; k<2; k++){
            for(int h = 0; h<actualGameStatus.getPieces()[k].size(); h++) {
                tempX = actualGameStatus.getPieces()[k].get(h).getxCoordinate();
                tempY = actualGameStatus.getPieces()[k].get(h).getyCoordinate();
                tempSymbol = actualGameStatus.getPieces()[k].get(h).getPieceSymbol();
                tempPiece = PieceFactory.createPiece(tempSymbol,tempX,tempY);
                tempPiece.setStatus(actualGameStatus.getPieces()[k].get(h).getStatus());
                pieces[k].add(tempPiece);
            }
        }
        newGame.setPieces(pieces);
        newGame.setVitalities(actualGameStatus.toString().substring(37,53));
        return newGame;
    }
}
