package it.polimi.group02.model;

import java.util.ArrayList;
import java.util.List;

import it.polimi.group02.model.utility.Utility;

import static it.polimi.group02.model.CopyGameStatus.copyGameStatus;
import static it.polimi.group02.model.utility.Color.BLACK;
import static it.polimi.group02.model.utility.Color.WHITE;
import static it.polimi.group02.model.utility.Utility.NUMBER_OF_PLAYERS;
import static it.polimi.group02.model.utility.Utility.SPECIAL_CELLS_RECORD;
import static it.polimi.group02.model.utility.Utility.archer_initial_vitality;
import static it.polimi.group02.model.utility.Utility.dragon_initial_vitality;
import static it.polimi.group02.model.utility.Utility.giant_initial_vitality;
import static it.polimi.group02.model.utility.Utility.knight_initial_vitality;
import static it.polimi.group02.model.utility.Utility.mage_initial_vitality;
import static it.polimi.group02.model.utility.Utility.squire_initial_vitality;
import static java.lang.Character.isUpperCase;

/**
 * Created by florin7 on 19/12/2016.
 */

public class ArtificialIntelligence {

    //the points are given as a value each piece can have, regarding the following
    //FORMULA: vitality + moveRange*1.5 + 2(if moveDirection any) + 4(if moveType fly) +
    //          + attackRange*1.5 + attackStrength*2 + 2(if diag attack) + 15(for the spells)
    // while when a piece lost some vitality it's new value is recomputed like:
    //FORMULA InitalPoints - lostVitality - (lostVitality/(InitialVitality*2))*InitialPoints
    //This weird formula helped me to not give too much importance since also other attributes are
    //really important in this game
    private static final double GIANT_POINTS = 17.5;
    private static final double DRAGON_POINTS = 23.5;
    private static final double ARCHER_POINTS = 18.5;
    private static final double MAGE_POINTS = 29.5;
    private static final double KNIGHT_POINTS = 14;
    private static final double SQUIRE_POINTS = 6.5;

    private static final char[] MOVE_TYPES = {'M','A','F','H','T','R'};
    private static final int ROW = 6;
    private static final int COLMN = 6;
    private static Integer recursionLevel = 1;//the higher the more time it takes to compute the next move

    private static final int[][] SPECIAL_CELL_1_ZONE = {{0,1},{1,0},{1,1}};
    private static final int[][] SPECIAL_CELL_2_ZONE = {{0,2},{0,4},{1,2},{1,3},{1,4}};
    private static final int[][] SPECIAL_CELL_4_ZONE = {{5,4},{4,5},{4,4}};
    private static final int[][] SPECIAL_CELL_3_ZONE = {{5,1},{4,1},{4,2},{5,3},{4,3}};
    private static final int[][][] SPECIAL_CELL_ZONES = {SPECIAL_CELL_1_ZONE,SPECIAL_CELL_2_ZONE,SPECIAL_CELL_3_ZONE,SPECIAL_CELL_4_ZONE};

    /**
     * This method prepares the Game Model to be evaluated from the input string
     * @param stringGame representation of the game in a string
     * @return the next move
     */
    public static String nextMove(String stringGame) {
        String move = "";
        GameModel actualGameStatus = createGameFromString(stringGame);
        move = calcualteNextBestMove(actualGameStatus, move);
        return move.substring(0, 5);
    }

    /**
     * The method requires as input the game status in the form of the game model
     * @param actualGameStatus
     * @param actualSetOfMoves
     * @return
     */
    public static String calcualteNextBestMove(GameModel actualGameStatus, String actualSetOfMoves){
        GameModel currentGame = copyGameStatus(actualGameStatus);
        ArrayList<String> interestingMoves = setInterestingMoves(actualGameStatus,false);
        ArrayList<String> winningMoves = new ArrayList<>();
        double maximumRecordPlayer1 = 0;
        double maximumRecordPlayer2 = 0;
        double minimumLossPlayer1 = -100;
        double minimumLossPlayer2 = -100;
        int tempRecordMaxP1 = -1;
        int tempRecordMaxP2 = -1;
        int tempRecordMinP1 = -1;
        int tempRecordMinP2 = -1;
        int tempRecordDrawP1 = -1;
        int tempRecordDrawP2 = -1;
        String firstWorking = "";//just in case of troubles to not make the game crash
        for(int i=0; i<interestingMoves.size(); i++) {//first check if you can immediately win
            GameModel newTemporaryGame = copyGameStatus(currentGame);
            newTemporaryGame.parseAction(interestingMoves.get(i));
            newTemporaryGame.repriseTurn();
            if (updatePlayersPoints(newTemporaryGame)[0] > 500 && currentGame.toString().charAt(0) == 'W'
                    || updatePlayersPoints(newTemporaryGame)[1] > 500 && currentGame.toString().charAt(0) == 'B') {
                return interestingMoves.get(i);
            }
        }
        for(int i=0; i<interestingMoves.size(); i++){
            GameModel newTemporaryGame = copyGameStatus(currentGame);
            newTemporaryGame.parseAction(interestingMoves.get(i));
            newTemporaryGame.repriseTurn();
            boolean recursionFOundMove = false;
            if(!newTemporaryGame.toString().contains("ERROR")){//check if the actual move can lead to a future vitory or loss
                String nextTurn = "";
                try{
                    nextTurn = nextTurnsOverview(copyGameStatus(newTemporaryGame),interestingMoves.get(i),copyGameStatus(actualGameStatus));
                }
                catch(StackOverflowError e){
                    recursionFOundMove = false;
                }
                if(nextTurn.length()>0 && !nextTurn.equals("$$$$$")){
                    if(!copyGameStatus(currentGame).parseAction(nextTurn.substring(3,8)).toString().contains("ERROR")) {
                        if (nextTurn.charAt(0) == '#') {
                            recursionFOundMove = true;
                            winningMoves.add(nextTurn);
                        }
                    }
                }
                if(!recursionFOundMove){
                    firstWorking = interestingMoves.get(i);
                    if (updatePlayersPoints(newTemporaryGame)[0] - updatePlayersPoints(newTemporaryGame)[1] >= 0) {
                        if (updatePlayersPoints(newTemporaryGame)[0] - updatePlayersPoints(newTemporaryGame)[1] > maximumRecordPlayer1) {
                            maximumRecordPlayer1 = updatePlayersPoints(newTemporaryGame)[0] - updatePlayersPoints(newTemporaryGame)[1];
                            tempRecordMaxP1 = i;
                        }
                        if (updatePlayersPoints(newTemporaryGame)[0] - updatePlayersPoints(newTemporaryGame)[1] == 0) {
                            tempRecordDrawP1 = i;
                        }
                    } else {
                        if (updatePlayersPoints(newTemporaryGame)[0] - updatePlayersPoints(newTemporaryGame)[1] > minimumLossPlayer1) {
                            minimumLossPlayer1 = updatePlayersPoints(newTemporaryGame)[0] - updatePlayersPoints(newTemporaryGame)[1];
                            tempRecordMinP1 = i;
                        }
                    }
                    if (updatePlayersPoints(newTemporaryGame)[1] - updatePlayersPoints(newTemporaryGame)[0] >= 0) {
                        if (updatePlayersPoints(newTemporaryGame)[1] - updatePlayersPoints(newTemporaryGame)[0] > maximumRecordPlayer2) {
                            maximumRecordPlayer2 = updatePlayersPoints(newTemporaryGame)[1] - updatePlayersPoints(newTemporaryGame)[0];
                            tempRecordMaxP2 = i;
                        }
                        if (updatePlayersPoints(newTemporaryGame)[1] - updatePlayersPoints(newTemporaryGame)[0] == 0) {
                            tempRecordDrawP2 = i;
                        }
                    } else {
                        if (updatePlayersPoints(newTemporaryGame)[1] - updatePlayersPoints(newTemporaryGame)[0] > minimumLossPlayer2) {
                            minimumLossPlayer2 = updatePlayersPoints(newTemporaryGame)[1] - updatePlayersPoints(newTemporaryGame)[0];
                            tempRecordMinP2 = i;
                        }
                    }
                }
            }

        }

        if(currentGame.toString().charAt(0) == 'W'){
            String teleportPiece = tryMoveOrTeleportOverFrozenPiece(copyGameStatus(actualGameStatus));
            teleportPiece = "#W#"+teleportPiece+"T";
            ArrayList<String> defendingMoves = new ArrayList<>();
            if(teleportPiece.length()>5) {
                winningMoves.add(teleportPiece);
            }
            for(int k=0; k<winningMoves.size(); k++){
                GameModel tempCopyGame = copyGameStatus(actualGameStatus);
                tempCopyGame.parseAction(winningMoves.get(k).substring(3,8));
                tempCopyGame.repriseTurn();
                if(updatePlayersPoints(tempCopyGame)[0] >= maximumRecordPlayer1){
                    actualSetOfMoves = winningMoves.get(k).substring(3,8);
                    maximumRecordPlayer1 = updatePlayersPoints(tempCopyGame)[0];
                }
                if(winningMoves.get(k).charAt(8)=='D'){
                    defendingMoves.add(winningMoves.get(k));
                }
            }
            if(defendingMoves.size()>0){
                for(int r=0; r<defendingMoves.size(); r++){
                    GameModel tempCopyGame = copyGameStatus(actualGameStatus);
                    tempCopyGame.parseAction(defendingMoves.get(r).substring(3,8));
                    tempCopyGame.repriseTurn();
                    if(updatePlayersPoints(tempCopyGame)[0] >= maximumRecordPlayer1){
                        actualSetOfMoves = defendingMoves.get(r).substring(3,8);
                        maximumRecordPlayer1 = updatePlayersPoints(tempCopyGame)[0];
                    }
                }
            }else{
                GameModel testRevive = copyGameStatus(currentGame);
                ArrayList<Piece> pieces1 = (ArrayList)testRevive.getPieces()[0];
                boolean hasDragon = false;
                for(int s=0; s<pieces1.size(); s++){
                    if (pieces1.get(s).getPieceSymbol()=='D'){
                        hasDragon = true;
                    }
                }
                if(hasDragon==false){
                    if(!testRevive.parseAction("R1300").contains("ERROR"))
                    {
                        return "R1300";
                    }
                }
            }
            if(actualSetOfMoves.length()>0){
                return actualSetOfMoves;
            }else {
                if (tempRecordMaxP1 >= 0) {
                    actualSetOfMoves = actualSetOfMoves + interestingMoves.get(tempRecordMaxP1);
                } else {
                    if (tempRecordDrawP1 >= 0) {
                        actualSetOfMoves = actualSetOfMoves + interestingMoves.get(tempRecordDrawP1);
                    } else {
                        if (tempRecordMinP1 >= 0) {
                            actualSetOfMoves = actualSetOfMoves + interestingMoves.get(tempRecordMinP1);
                        } else {
                            actualSetOfMoves = actualSetOfMoves + firstWorking;
                        }
                    }
                }
            }
        }else if(currentGame.toString().charAt(0) == 'B'){
            String teleportPiece = tryMoveOrTeleportOverFrozenPiece(copyGameStatus(actualGameStatus));
            teleportPiece = "#B#"+teleportPiece+"T";
            ArrayList<String> defendingMoves = new ArrayList<>();
            if(teleportPiece.length()>5) {
                winningMoves.add(teleportPiece);
            }
            for(int k=0; k<winningMoves.size(); k++){
                GameModel tempCopyGame = copyGameStatus(actualGameStatus);
                tempCopyGame.parseAction(winningMoves.get(k).substring(3,8));
                tempCopyGame.repriseTurn();
                if(updatePlayersPoints(tempCopyGame)[1] >= maximumRecordPlayer2){
                    actualSetOfMoves = winningMoves.get(k).substring(3,8);
                    maximumRecordPlayer2 = updatePlayersPoints(tempCopyGame)[1];
                }
                if(winningMoves.get(k).charAt(8)=='D'){
                    defendingMoves.add(winningMoves.get(k));
                }
            }
            if(defendingMoves.size()>0){
                for(int r=0; r<defendingMoves.size(); r++){
                    GameModel tempCopyGame = copyGameStatus(actualGameStatus);
                    tempCopyGame.parseAction(defendingMoves.get(r).substring(3,8));
                    tempCopyGame.repriseTurn();
                    if(updatePlayersPoints(tempCopyGame)[1] >= maximumRecordPlayer2){
                        actualSetOfMoves = defendingMoves.get(r).substring(3,8);
                        maximumRecordPlayer2 = updatePlayersPoints(tempCopyGame)[1];
                    }
                }
            }else {
                GameModel testRevive = copyGameStatus(currentGame);
                ArrayList<Piece> pieces1 = (ArrayList) testRevive.getPieces()[1];
                boolean hasDragon = false;
                for (int s = 0; s < pieces1.size(); s++) {
                    if (pieces1.get(s).getPieceSymbol() == 'd') {
                        hasDragon = true;
                    }
                }
                if (hasDragon == false) {
                    if (!testRevive.parseAction("R6400").contains("ERROR")) {
                        return "R6400";
                    }
                }
            }
            if(actualSetOfMoves.length()>0){
                return actualSetOfMoves;
            }else {
                if (tempRecordMaxP2 >= 0) {
                    actualSetOfMoves = actualSetOfMoves + interestingMoves.get(tempRecordMaxP2);
                } else {
                    if (tempRecordDrawP2 >= 0) {
                        actualSetOfMoves = actualSetOfMoves + interestingMoves.get(tempRecordDrawP2);
                    } else {
                        if (tempRecordMinP2 >= 0) {
                            actualSetOfMoves = actualSetOfMoves + interestingMoves.get(tempRecordMinP2);
                        } else {
                            actualSetOfMoves = actualSetOfMoves + firstWorking;
                        }
                    }
                }
            }
        }
        return actualSetOfMoves;
    }

    private static String tryMoveOrTeleportOverFrozenPiece(GameModel gameModel) {
        if(gameModel.toString().charAt(0)=='W'){
            String frozenPieceInformation = gameModel.getPlayers()[1].getFrozenPieceInformation();
            if(frozenPieceInformation.charAt(0)!='0') {
                if (gameModel.getPlayers()[0].getUnusedSpells().contains("T")) {
                    for (int i = 0; i < gameModel.getPieces()[0].size(); i++) {
                        if (gameModel.getPieces()[0].get(i).getPieceSymbol() == 'G') {
                            if (!GameModel.isSpecialCell(gameModel.getPieces()[0].get(i).getxCoordinate(),
                                    gameModel.getPieces()[0].get(i).getyCoordinate())) {
                                String returnString = "T" + Integer.toString(gameModel.getPieces()[0].get(i).getyCoordinate() + 1) +
                                        Integer.toString(gameModel.getPieces()[0].get(i).getxCoordinate()+ 1) +
                                        Character.toString(frozenPieceInformation.charAt(0)) + Character.toString(frozenPieceInformation.charAt(1));
                                return returnString;
                            }
                        }
                        if (gameModel.getPieces()[0].get(i).getPieceSymbol() == 'K') {
                            if (!GameModel.isSpecialCell(gameModel.getPieces()[0].get(i).getxCoordinate(),
                                    gameModel.getPieces()[0].get(i).getyCoordinate())) {
                                String returnString = "T" + Integer.toString(gameModel.getPieces()[0].get(i).getyCoordinate() + 1) +
                                        Integer.toString(gameModel.getPieces()[0].get(i).getxCoordinate() + 1) +
                                        Character.toString(frozenPieceInformation.charAt(0)) + Character.toString(frozenPieceInformation.charAt(1));
                                return returnString;
                            }
                        }
                        if (gameModel.getPieces()[0].get(i).getPieceSymbol() == 'S') {
                            if (!GameModel.isSpecialCell(gameModel.getPieces()[0].get(i).getxCoordinate(),
                                    gameModel.getPieces()[0].get(i).getyCoordinate())) {
                                String returnString = "T" + Integer.toString(gameModel.getPieces()[0].get(i).getyCoordinate() + 1) +
                                        Integer.toString(gameModel.getPieces()[0].get(i).getxCoordinate() + 1) +
                                        Character.toString(frozenPieceInformation.charAt(0)) + Character.toString(frozenPieceInformation.charAt(1));
                                return returnString;
                            }
                        }
                    }
                }
                for (int i = 0; i < gameModel.getPieces()[0].size(); i++) {
                    int yC = gameModel.getPieces()[0].get(i).getyCoordinate();
                    int xC = gameModel.getPieces()[0].get(i).getxCoordinate();
                    String returnString = "M" + Integer.toString(yC + 1) + Integer.toString(xC + 1) +
                            Character.toString(frozenPieceInformation.charAt(0)) + Character.toString(frozenPieceInformation.charAt(1));
                    GameModel testGame = copyGameStatus(gameModel);
                    testGame.parseAction(returnString);
                    if(!testGame.toString().contains("ERROR")){
                        return returnString;
                    }
                }
            }
        }
        if(gameModel.toString().charAt(0)=='B'){
            String frozenPieceInformation = gameModel.getPlayers()[0].getFrozenPieceInformation();
            if(frozenPieceInformation.charAt(0)!='0') {
                if (gameModel.getPlayers()[1].getUnusedSpells().contains("T")) {
                    for (int i = 0; i < gameModel.getPieces()[1].size(); i++) {
                        if (gameModel.getPieces()[1].get(i).getPieceSymbol() == 'G') {
                            if (!GameModel.isSpecialCell(gameModel.getPieces()[1].get(i).getxCoordinate(),
                                    gameModel.getPieces()[1].get(i).getyCoordinate())) {
                                String returnString = "T" + Integer.toString(gameModel.getPieces()[1].get(i).getyCoordinate() + 1) +
                                        Integer.toString(gameModel.getPieces()[1].get(i).getxCoordinate() + 1) +
                                        Character.toString(frozenPieceInformation.charAt(0)) + Character.toString(frozenPieceInformation.charAt(1));
                                return returnString;
                            }
                        }
                        if (gameModel.getPieces()[1].get(i).getPieceSymbol() == 'K') {
                            if (!GameModel.isSpecialCell(gameModel.getPieces()[1].get(i).getxCoordinate(),
                                   gameModel.getPieces()[1].get(i).getyCoordinate())) {
                                String returnString = "T" + Integer.toString(gameModel.getPieces()[1].get(i).getyCoordinate() + 1) +
                                        Integer.toString(gameModel.getPieces()[1].get(i).getxCoordinate() + 1) +
                                        Character.toString(frozenPieceInformation.charAt(0)) + Character.toString(frozenPieceInformation.charAt(1));
                                return returnString;
                            }
                        }
                        if (gameModel.getPieces()[1].get(i).getPieceSymbol() == 'S') {
                            if (!GameModel.isSpecialCell(gameModel.getPieces()[1].get(i).getxCoordinate(),
                                    gameModel.getPieces()[1].get(i).getyCoordinate())) {
                                String returnString = "T" + Integer.toString(gameModel.getPieces()[1].get(i).getyCoordinate() + 1) +
                                        Integer.toString(gameModel.getPieces()[1].get(i).getxCoordinate() + 1) +
                                        Character.toString(frozenPieceInformation.charAt(0)) + Character.toString(frozenPieceInformation.charAt(1));
                                return returnString;
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < gameModel.getPieces()[1].size(); i++) {
                String returnString = "M" + Integer.toString(gameModel.getPieces()[1].get(i).getyCoordinate() + 1) +
                        Integer.toString(gameModel.getPieces()[1].get(i).getxCoordinate() + 1) +
                        Character.toString(frozenPieceInformation.charAt(0)) + Character.toString(frozenPieceInformation.charAt(1));
                GameModel testGame = copyGameStatus(gameModel);
                testGame.parseAction(returnString);
                if(!testGame.toString().contains("ERROR")){
                    return returnString;
                }
            }
        }
        return "";
    }

    private static void removeMovesThatRemovePiecesFromTheSpecialCells(ArrayList<String> interestingMoves, GameModel newGameCopy) {
        String toBeRemoved = "#";
        for(int i=0; i<interestingMoves.size(); i++){
            for(int c=0; c<SPECIAL_CELL_ZONES.length; c++) {
                for (int s = 0; s < SPECIAL_CELL_ZONES[c].length; s++) {
                    if (newGameCopy.getCurrentConfiguration()[SPECIAL_CELL_ZONES[c][s][0]][SPECIAL_CELL_ZONES[c][s][1]] != '0') {
                         toBeRemoved = "M"+Integer.toString(SPECIAL_CELL_ZONES[c][s][1]+1)+Integer.toString(SPECIAL_CELL_ZONES[c][s][0]+1);
                    }
                    for(int k=0; k<interestingMoves.size(); k++){
                        if(interestingMoves.get(k).contains(toBeRemoved)){
                            interestingMoves.remove(k);
                        }
                    }
                }
            }
        }
    }

    private static double[] updatePlayersPoints(GameModel newGameCopy) {
        double[] playersPoints = new double[2];
        int numerOfSpecialOccupiedPlayer1 = 0;
        int numerOfSpecialOccupiedPlayer2 = 0;
        int tempVit;
        playersPoints[0] = 0;
        playersPoints[1] = 0;
        //USING A MATHEMATICAL FORMULA REGARDING THE POINTS OF ATTACKS, MOVES AND VITALITY TO CALCULATE POINTS OF PLAYERS
        for(int k=0; k<2; k++){
            for(int i=0; i<newGameCopy.getPieces()[k].size(); i++){
                if(newGameCopy.getPieces()[k].get(i).getPieceSymbol() == 'G' || (newGameCopy.getPieces()[k].get(i).getPieceSymbol() == 'g')){
                    tempVit = newGameCopy.getPieces()[k].get(i).getVitality();
                    if(tempVit>0) {
                        playersPoints[k] = playersPoints[k] + (GIANT_POINTS - (Utility.giant_initial_vitality - tempVit) - (tempVit / (Utility.giant_initial_vitality * 2)) * GIANT_POINTS);
                    }
                }
                if(newGameCopy.getPieces()[k].get(i).getPieceSymbol() == 'M' || (newGameCopy.getPieces()[k].get(i).getPieceSymbol() == 'm')){
                    tempVit = newGameCopy.getPieces()[k].get(i).getVitality();
                    if(tempVit>0) {
                        if (!newGameCopy.getPlayers()[k].getUnusedSpells().contains("F")) {
                            tempVit = tempVit - 4;
                        }
                        if (!newGameCopy.getPlayers()[k].getUnusedSpells().contains("R")) {
                            tempVit = tempVit - 5;
                        }
                        if (!newGameCopy.getPlayers()[k].getUnusedSpells().contains("T")) {
                            tempVit = tempVit - 3;
                        }
                        if (!newGameCopy.getPlayers()[k].getUnusedSpells().contains("H")) {
                            tempVit = tempVit - 3;
                        }
                        if (!newGameCopy.getPlayers()[k].getUnusedSpells().contains("F") && !newGameCopy.getPlayers()[k].getUnusedSpells().contains("H")
                                && !newGameCopy.getPlayers()[k].getUnusedSpells().contains("R") && !newGameCopy.getPlayers()[k].getUnusedSpells().contains("T")) {
                            tempVit = tempVit - 3;
                        }
                        playersPoints[k] = playersPoints[k] + (MAGE_POINTS - (Utility.mage_initial_vitality - tempVit) - (tempVit / (Utility.mage_initial_vitality * 2)) * MAGE_POINTS);
                    }
                }
                if(newGameCopy.getPieces()[k].get(i).getPieceSymbol() == 'A' || (newGameCopy.getPieces()[k].get(i).getPieceSymbol() == 'a')){
                    tempVit = newGameCopy.getPieces()[k].get(i).getVitality();
                    if(tempVit>0) {
                        playersPoints[k] = playersPoints[k] + (ARCHER_POINTS - (Utility.archer_initial_vitality - tempVit) - (tempVit / (Utility.archer_initial_vitality * 2)) * ARCHER_POINTS);
                    }
                }
                if(newGameCopy.getPieces()[k].get(i).getPieceSymbol() == 'D' || (newGameCopy.getPieces()[k].get(i).getPieceSymbol() == 'd')){
                    tempVit = newGameCopy.getPieces()[k].get(i).getVitality();
                    if(tempVit>0) {
                        playersPoints[k] = playersPoints[k] + (DRAGON_POINTS - (Utility.dragon_initial_vitality - tempVit) - (tempVit / (Utility.dragon_initial_vitality * 2)) * DRAGON_POINTS);
                    }
                }
                if(newGameCopy.getPieces()[k].get(i).getPieceSymbol() == 'K' || (newGameCopy.getPieces()[k].get(i).getPieceSymbol() == 'k')){
                    tempVit = newGameCopy.getPieces()[k].get(i).getVitality();
                    if(tempVit>0) {
                        playersPoints[k] = playersPoints[k] + (KNIGHT_POINTS - (knight_initial_vitality - tempVit) - (tempVit / (knight_initial_vitality * 2)) * KNIGHT_POINTS);
                    }
                }
                if(newGameCopy.getPieces()[k].get(i).getPieceSymbol() == 'S' || (newGameCopy.getPieces()[k].get(i).getPieceSymbol() == 's')){
                    tempVit = newGameCopy.getPieces()[k].get(i).getVitality();
                    if(tempVit>0) {
                        playersPoints[k] = playersPoints[k] + (SQUIRE_POINTS - (Utility.squire_initial_vitality - tempVit) - (tempVit / (Utility.squire_initial_vitality * 2)) * SQUIRE_POINTS);
                    }
                }
            }
        }
        //COUNTING FOR EACH PLAYER THE NUMBER OF SPECIAL CELLS OCCUPIED
        for(int s=0; s<Utility.SPECIAL_CELLS_RECORD.length; s++){
            if(newGameCopy.getCurrentConfiguration()[SPECIAL_CELLS_RECORD[s][0]][SPECIAL_CELLS_RECORD[s][1]] != '0') {
                if (isUpperCase(newGameCopy.getCurrentConfiguration()[SPECIAL_CELLS_RECORD[s][0]][SPECIAL_CELLS_RECORD[s][1]])) {
                    numerOfSpecialOccupiedPlayer1++;
                }
                if (!isUpperCase(newGameCopy.getCurrentConfiguration()[SPECIAL_CELLS_RECORD[s][0]][SPECIAL_CELLS_RECORD[s][1]])) {
                    numerOfSpecialOccupiedPlayer2++;
                }
            }
        }
        //ADDING BIG POINTS IF OCCUPY SPECIAL CELL
        if(numerOfSpecialOccupiedPlayer1==1){
            playersPoints[0] = playersPoints[0] + 100;
        }else if(numerOfSpecialOccupiedPlayer1==2){
            playersPoints[0] = playersPoints[0] + 200;
        }else if(numerOfSpecialOccupiedPlayer1==3){
            playersPoints[0] = playersPoints[0] + 1000;
        }
        if(numerOfSpecialOccupiedPlayer2==1){
            playersPoints[1] = playersPoints[1] + 100;
        }else if(numerOfSpecialOccupiedPlayer2==2){
            playersPoints[1] = playersPoints[1] + 200;
        }else if(numerOfSpecialOccupiedPlayer2==3){
            playersPoints[1] = playersPoints[1] + 1000;
        }

        //ADDING POINTS IF YOU TRY GO NEAR A SPECIAL CELL YOU ARE GIVEN MORE EXTRA POINTS DEPENDING ALSO OF THE STRENGTH OF THE PIECE
        for(int c=0; c<SPECIAL_CELL_ZONES.length; c++) {
            for (int s = 0; s < SPECIAL_CELL_ZONES[c].length; s++) {
                if (newGameCopy.getCurrentConfiguration()[SPECIAL_CELL_ZONES[c][s][0]][SPECIAL_CELL_ZONES[c][s][1]] != '0') {
                    if (isUpperCase(newGameCopy.getCurrentConfiguration()[SPECIAL_CELL_ZONES[c][s][0]][SPECIAL_CELL_ZONES[c][s][1]])) {
                        double multiplyFactor = 1;
                        if(!isUpperCase((newGameCopy.getCurrentConfiguration()[SPECIAL_CELLS_RECORD[c][0]][SPECIAL_CELLS_RECORD[c][1]]))
                                && newGameCopy.getCurrentConfiguration()[SPECIAL_CELLS_RECORD[c][0]][SPECIAL_CELLS_RECORD[c][1]] != '0'){
                            multiplyFactor = 1.5;
                        }
                        int pieceVitality = newGameCopy.getPieceAtTurn(0, SPECIAL_CELL_ZONES[c][s][1], SPECIAL_CELL_ZONES[c][s][0]).getVitality();
                        char pieceSimbol = newGameCopy.getPieceAtTurn(0, SPECIAL_CELL_ZONES[c][s][1], SPECIAL_CELL_ZONES[c][s][0]).getPieceSymbol();
                        if (pieceSimbol == 'G') {
                            playersPoints[0] = playersPoints[0] + multiplyFactor*GIANT_POINTS * (pieceVitality / giant_initial_vitality) / 2;
                            playersPoints[1] = playersPoints[1] - multiplyFactor*GIANT_POINTS * (pieceVitality / giant_initial_vitality) / 2;
                        }
                        if (pieceSimbol == 'K') {
                            playersPoints[0] = playersPoints[0] + multiplyFactor*KNIGHT_POINTS * (pieceVitality / knight_initial_vitality) / 2;
                            playersPoints[1] = playersPoints[1] - multiplyFactor*KNIGHT_POINTS * (pieceVitality / knight_initial_vitality) / 2;
                        }
                        if (pieceSimbol == 'M') {
                            playersPoints[0] = playersPoints[0] + multiplyFactor*MAGE_POINTS * (pieceVitality / mage_initial_vitality) / 2;
                            playersPoints[1] = playersPoints[1] - multiplyFactor*MAGE_POINTS * (pieceVitality / mage_initial_vitality) / 2;
                        }
                        if (pieceSimbol == 'A') {
                            playersPoints[0] = playersPoints[0] + multiplyFactor*ARCHER_POINTS * (pieceVitality / archer_initial_vitality) / 2;
                            playersPoints[1] = playersPoints[1] - multiplyFactor*ARCHER_POINTS * (pieceVitality / archer_initial_vitality) / 2;
                        }
                        if (pieceSimbol == 'D') {
                            playersPoints[0] = playersPoints[0] + multiplyFactor * DRAGON_POINTS * (pieceVitality / dragon_initial_vitality) / 2;
                            playersPoints[1] = playersPoints[1] - multiplyFactor * DRAGON_POINTS * (pieceVitality / dragon_initial_vitality) / 2;
                        }
                        if (pieceSimbol == 'S') {
                            playersPoints[0] = playersPoints[0] + multiplyFactor*SQUIRE_POINTS * (pieceVitality / squire_initial_vitality) / 2;
                            playersPoints[1] = playersPoints[1] - multiplyFactor*SQUIRE_POINTS * (pieceVitality / squire_initial_vitality) / 2;
                        }

                    }
                    if (!isUpperCase(newGameCopy.getCurrentConfiguration()[SPECIAL_CELL_ZONES[c][s][0]][SPECIAL_CELL_ZONES[c][s][1]])) {
                        int pieceVitality = newGameCopy.getPieceAtTurn(1, SPECIAL_CELL_ZONES[c][s][1], SPECIAL_CELL_ZONES[c][s][0]).getVitality();
                        char pieceSimbol = newGameCopy.getPieceAtTurn(1, SPECIAL_CELL_ZONES[c][s][1], SPECIAL_CELL_ZONES[c][s][0]).getPieceSymbol();
                        double multiplyFactor = 1;
                        if(isUpperCase((newGameCopy.getCurrentConfiguration()[SPECIAL_CELLS_RECORD[c][0]][SPECIAL_CELLS_RECORD[c][1]]))
                                && newGameCopy.getCurrentConfiguration()[SPECIAL_CELLS_RECORD[c][0]][SPECIAL_CELLS_RECORD[c][1]] != '0'){
                            multiplyFactor = 1.5;
                        }
                        if (pieceSimbol == 'g') {
                            playersPoints[1] = playersPoints[1] + multiplyFactor*GIANT_POINTS * (pieceVitality / giant_initial_vitality) / 2;
                            playersPoints[0] = playersPoints[0] - multiplyFactor*GIANT_POINTS * (pieceVitality / giant_initial_vitality) / 2;
                        }
                        if (pieceSimbol == 'k') {
                            playersPoints[1] = playersPoints[1] + multiplyFactor*KNIGHT_POINTS * (pieceVitality / knight_initial_vitality) / 2;
                            playersPoints[0] = playersPoints[0] - multiplyFactor*KNIGHT_POINTS * (pieceVitality / knight_initial_vitality) / 2;
                        }
                        if (pieceSimbol == 'm') {
                            playersPoints[1] = playersPoints[1] + multiplyFactor*MAGE_POINTS * (pieceVitality / mage_initial_vitality) / 2;
                            playersPoints[0] = playersPoints[0] - multiplyFactor*MAGE_POINTS * (pieceVitality / mage_initial_vitality) / 2;
                        }
                        if (pieceSimbol == 'a') {
                            playersPoints[1] = playersPoints[1] + multiplyFactor*ARCHER_POINTS * (pieceVitality / archer_initial_vitality) / 2;
                            playersPoints[0] = playersPoints[0] - multiplyFactor*ARCHER_POINTS * (pieceVitality / archer_initial_vitality) / 2;
                        }
                        if (pieceSimbol == 'd') {
                            playersPoints[1] = playersPoints[1] + multiplyFactor*DRAGON_POINTS * (pieceVitality / dragon_initial_vitality) / 2;
                            playersPoints[0] = playersPoints[0] - multiplyFactor*DRAGON_POINTS * (pieceVitality / dragon_initial_vitality) / 2;
                        }
                        if (pieceSimbol == 's') {
                            playersPoints[1] = playersPoints[1] + multiplyFactor*SQUIRE_POINTS * (pieceVitality / squire_initial_vitality) / 2;
                            playersPoints[0] = playersPoints[0] - multiplyFactor*SQUIRE_POINTS * (pieceVitality / squire_initial_vitality) / 2;
                        }
                    }
                }
            }
        }
        return playersPoints;
    }

    private static ArrayList<String> setInterestingMoves(GameModel newGameModel, boolean calledInDefending) {
        ArrayList<String> newInterestingMoves = new ArrayList<>();
        String newMove;
        boolean heal = healGain(newGameModel);
        boolean revove = reviveGain(newGameModel);
        for(int h=0; h<6; h++) {
            for (int i = 1; i <=COLMN; i++) {
                for (int j = 1; j <=ROW; j++) {
                    for(int k = 1; k<=COLMN; k++) {
                        for (int l = 1; l <=ROW; l++) {
                            if(i!=k && j!=l) {
                                if (MOVE_TYPES[h] == 'M' || MOVE_TYPES[h] == 'A') {
                                    if(!calledInDefending) {
                                        if (!GameModel.isSpecialCell(j - 1, i - 1)) {//ADD ONLY MOVES THAT DON'T REGARD MOVING AWAY FROM SPECIAL CELLS
                                            //    if ((l <= 2 || l >= 5) && ((l <= 2 && k < 6) || (l >= 5 && k >= 1))) {
                                            newMove = Character.toString(MOVE_TYPES[h]) + Integer.toString(i) + Integer.toString(j) + Integer.toString(k) + Integer.toString(l);
                                            newInterestingMoves.add(newMove);
                                            //     }
                                        }
                                    }else{
                                        newMove = Character.toString(MOVE_TYPES[h]) + Integer.toString(i) + Integer.toString(j) + Integer.toString(k) + Integer.toString(l);
                                        newInterestingMoves.add(newMove);
                                    }
                                }

                                if (MOVE_TYPES[h] == 'H' || MOVE_TYPES[h] == 'R') {
                                    if (k == 0 && l == 0) {
                                        if(revove){
                                            System.err.println(revove);
                                            newMove = Character.toString(MOVE_TYPES[h]) + Integer.toString(i) + Integer.toString(j) + Integer.toString(k) + Integer.toString(l);
                                            newInterestingMoves.add(newMove);
                                        }
                                        if(heal){
                                            newMove = Character.toString(MOVE_TYPES[h]) + Integer.toString(i) + Integer.toString(j) + Integer.toString(k) + Integer.toString(l);
                                            newInterestingMoves.add(newMove);
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        return newInterestingMoves;
    }

    private static boolean reviveGain(GameModel currentGame) {
        int maxLife = 0;
        if(currentGame.toString().charAt(0)=='W'){
            maxLife = 0;
            for(int k=0; k<currentGame.getPieces()[0].size(); k++){
                if(currentGame.getPieces()[0].get(k).getPieceSymbol()!='M'){
                    maxLife = currentGame.getPieces()[0].get(k).getVitality();
                }
            }
        }
        if(currentGame.toString().charAt(0)=='B'){
            maxLife = 0;
            for(int k=0; k<currentGame.getPieces()[1].size(); k++){
                if(currentGame.getPieces()[1].get(k).getPieceSymbol()!='m'){
                    maxLife = currentGame.getPieces()[1].get(k).getVitality();
                }
            }
        }
        if(maxLife<6){
            return true;
        }
        return false;
    }

    private static boolean healGain(GameModel currentGame) {
        int maxLife = 0;
        if(currentGame.toString().charAt(0)=='W'){
            maxLife = 0;
            for(int k=0; k<currentGame.getPieces()[0].size(); k++){
                if(currentGame.getPieces()[0].get(k).getPieceSymbol()!='M'){
                    maxLife = currentGame.getPieces()[0].get(k).getVitality();
                }
            }
        }
        if(currentGame.toString().charAt(0)=='B'){
            maxLife = 0;
            for(int k=0; k<currentGame.getPieces()[1].size(); k++){
                if(currentGame.getPieces()[1].get(k).getPieceSymbol()!='m'){
                    maxLife = currentGame.getPieces()[1].get(k).getVitality();
                }
            }
        }
        if(maxLife<4){
            return true;
        }
        return false;
    }

    private static String nextTurnsOverview(GameModel actualGameStatus, String startingMove, GameModel originalGameStatus) {
        String move = "";
        List<Object> moveAndGame = new ArrayList<>();
        ArrayList<GameModel> allGames = new ArrayList<>();
        ArrayList<String> moves = new ArrayList<>();
        ArrayList<Integer> recursion = new ArrayList<>();
        GameModel newGame = copyGameStatus(actualGameStatus);
        int numberOfGames = 0;
        boolean winnerFoud = false;
        moves.add(move);
        allGames.add(newGame);
        int recursion1 = 0;
        recursion.add(recursion1);
        //Arraylist
        moveAndGame.add(allGames);
        moveAndGame.add(moves);
        moveAndGame.add(recursion);
        moveAndGame.add(numberOfGames);
        moveAndGame.add(winnerFoud);
        //Calculate new situations
        moveAndGame = calculateAllPossibleSituations(moveAndGame);
        if(moveAndGame.size()<4) {
            move = setDefendingSpecialCellMoves(actualGameStatus,startingMove+(String)moveAndGame.get(1),originalGameStatus);
        }
        return move;
    }

    private static String setDefendingSpecialCellMoves(GameModel actualGameStatus, String selectionOfMoves, GameModel originalGameStatus) {
        GameModel newGame = copyGameStatus(actualGameStatus);
        char startingPlayer = actualGameStatus.toString().charAt(0);
        int moveSize = selectionOfMoves.length()/5;
        ArrayList<String> specialCellToDefend;
        for(int i=1; i<moveSize; i++) {
            newGame.parseAction(selectionOfMoves.substring((i) * 5, (i + 1) * 5));
            newGame.repriseTurn();
            if (startingPlayer == 'B') {
                if (updatePlayersPoints(newGame)[0] > 500) {
                    return "#W#" + selectionOfMoves.substring(0, 5) + "Attack";
                }
                if (updatePlayersPoints(newGame)[1] > 500) {
                    specialCellToDefend = searchLostSpecialCell(actualGameStatus, selectionOfMoves);
                    if (actualGameStatus.getPlayers()[0].getUnusedSpells().contains("F") && specialCellToDefend.size()>0) {
                        int y = Character.getNumericValue(specialCellToDefend.get(0).charAt(1));
                        int x = Character.getNumericValue(specialCellToDefend.get(0).charAt(2));
                        return "#W#" + "F" + Integer.toString(y) + Integer.toString(x) + "00" + "Defending";
                    }
                    if (actualGameStatus.getPlayers()[0].getUnusedSpells().contains("T") && specialCellToDefend.size()>0) {
                        int[] enemyDest = searchePieceToBeTeleportedOverEnemy(actualGameStatus);
                        int y = Character.getNumericValue(specialCellToDefend.get(0).charAt(1));
                        int x = Character.getNumericValue(specialCellToDefend.get(0).charAt(2));
                        if (enemyDest[0] >= 0) {
                            return "#W#" + "T" + Integer.toString(enemyDest[1] + 1) + Integer.toString(enemyDest[0] + 1) + Integer.toString(y) + Integer.toString(x) + "Defending";
                        } else {
                            //return "$$$$$";//impossible to defend with teleport
                        }
                    }
                    ArrayList<String> movesToBeTried = setInterestingMoves(originalGameStatus,false);
                    for(int r=0; r<movesToBeTried.size(); r++){
                        GameModel newGameCopy = copyGameStatus(originalGameStatus);
                        char charBeforeMove = newGameCopy.getCurrentConfiguration()[Character.getNumericValue(selectionOfMoves.substring((i) * 5, (i + 1) * 5).charAt(1))-1][
                                Character.getNumericValue(selectionOfMoves.substring((i) * 5, (i + 1) * 5).charAt(2))-1];
                        newGameCopy.parseAction(movesToBeTried.get(r));
                        if(!newGameCopy.toString().contains("ERROR")){
                            if(newGameCopy.getCurrentConfiguration()[Character.getNumericValue(selectionOfMoves.substring((i) * 5, (i + 1) * 5).charAt(1))-1][
                                    Character.getNumericValue(selectionOfMoves.substring((i) * 5, (i + 1) * 5).charAt(2))-1] != charBeforeMove){
                                return "#B#" +movesToBeTried.get(r)+"Defending";
                            }
                        }
                    }
                }
            }
            if (startingPlayer == 'W') {
                if (updatePlayersPoints(newGame)[0] > 500) {
                    specialCellToDefend = searchLostSpecialCell(actualGameStatus, selectionOfMoves);
                    if (actualGameStatus.getPlayers()[1].getUnusedSpells().contains("F") && specialCellToDefend.size()>0) {
                        int y = Character.getNumericValue(specialCellToDefend.get(0).charAt(1));
                        int x = Character.getNumericValue(specialCellToDefend.get(0).charAt(2));
                        return "#B#" + "F" + Integer.toString(y) + Integer.toString(x) + "00" + "Defending";
                    }
                    if (actualGameStatus.getPlayers()[0].getUnusedSpells().contains("T") && specialCellToDefend.size()>0) {
                        int[] enemyDest = searchePieceToBeTeleportedOverEnemy(actualGameStatus);
                        int y = Character.getNumericValue(specialCellToDefend.get(0).charAt(1));
                        int x = Character.getNumericValue(specialCellToDefend.get(0).charAt(2));
                        if (enemyDest[0] >= 0) {
                            return "#B#" + "T" + Integer.toString(enemyDest[1] + 1) + Integer.toString(enemyDest[0] + 1) + Integer.toString(y) + Integer.toString(x) + "Defending";
                        } else {
                            //return "$$$$$";//impossible to defend with teleport
                        }
                    }
                    ArrayList<String> movesToBeTried = setInterestingMoves(originalGameStatus,true);
                    for(int r=0; r<movesToBeTried.size(); r++){
                        GameModel newGameCopy = copyGameStatus(originalGameStatus);
                        char charBeforeMove = newGameCopy.getCurrentConfiguration()[Character.getNumericValue(selectionOfMoves.substring((i) * 5, (i + 1) * 5).charAt(1))-1][
                                Character.getNumericValue(selectionOfMoves.substring((i) * 5, (i + 1) * 5).charAt(2))-1];
                        newGameCopy.parseAction(movesToBeTried.get(r));
                        if(!newGameCopy.toString().contains("ERROR")){
                            if(newGameCopy.getCurrentConfiguration()[Character.getNumericValue(selectionOfMoves.substring((i) * 5, (i + 1) * 5).charAt(1))-1][
                            Character.getNumericValue(selectionOfMoves.substring((i) * 5, (i + 1) * 5).charAt(2))-1] != charBeforeMove){
                                return "#B#" +movesToBeTried.get(r)+"Defending";
                            }
                        }
                    }
                }
                if (updatePlayersPoints(newGame)[1] > 500) {
                    return "#B#" + selectionOfMoves.substring(0, 5) + "Attack";
                }
            }
        }
        return "$$$$$";
    }

    private static int[] searchePieceToBeTeleportedOverEnemy(GameModel actualGameStatus) {
        int[] coordinate = {-1,-1};
        boolean knightFound = false;
        boolean giantFound = false;
        if(actualGameStatus.toString().charAt(0)=='B'){
            for(int i=0; i<actualGameStatus.getPieces()[0].size(); i++){
                if(actualGameStatus.getPieces()[0].get(i).getPieceSymbol()=='K' &&
                        !GameModel.isSpecialCell(actualGameStatus.getPieces()[0].get(i).getxCoordinate(),(actualGameStatus.getPieces()[0].get(i).getyCoordinate()))){
                    knightFound = true;
                    coordinate[0] = actualGameStatus.getPieces()[0].get(i).getxCoordinate();
                    coordinate[1] = actualGameStatus.getPieces()[0].get(i).getyCoordinate();
                }
                if(!knightFound){
                    if(actualGameStatus.getPieces()[0].get(i).getPieceSymbol()=='G' &&
                            !GameModel.isSpecialCell(actualGameStatus.getPieces()[0].get(i).getxCoordinate(),(actualGameStatus.getPieces()[0].get(i).getyCoordinate()))){
                        giantFound = true;
                        coordinate[0] = actualGameStatus.getPieces()[0].get(i).getxCoordinate();
                        coordinate[1] = actualGameStatus.getPieces()[0].get(i).getyCoordinate();
                    }
                }
                if(!giantFound){
                    if(actualGameStatus.getPieces()[0].get(i).getPieceSymbol()=='D' &&
                            !GameModel.isSpecialCell(actualGameStatus.getPieces()[0].get(i).getxCoordinate(),(actualGameStatus.getPieces()[0].get(i).getyCoordinate()))){
                        coordinate[0] = actualGameStatus.getPieces()[0].get(i).getxCoordinate();
                        coordinate[1] = actualGameStatus.getPieces()[0].get(i).getyCoordinate();
                    }
                }
            }

        }
        if(actualGameStatus.toString().charAt(0)=='W'){
            for(int i=0; i<actualGameStatus.getPieces()[1].size(); i++){
                if(actualGameStatus.getPieces()[1].get(i).getPieceSymbol()=='k' &&
                        !GameModel.isSpecialCell(actualGameStatus.getPieces()[1].get(i).getxCoordinate(),(actualGameStatus.getPieces()[1].get(i).getyCoordinate()))){
                    knightFound = true;
                    coordinate[0] = actualGameStatus.getPieces()[1].get(i).getxCoordinate();
                    coordinate[1] = actualGameStatus.getPieces()[1].get(i).getyCoordinate();
                }
                if(!knightFound){
                    if(actualGameStatus.getPieces()[0].get(i).getPieceSymbol()=='g' &&
                            !GameModel.isSpecialCell(actualGameStatus.getPieces()[1].get(i).getxCoordinate(),(actualGameStatus.getPieces()[1].get(i).getyCoordinate()))){
                        giantFound = true;
                        coordinate[0] = actualGameStatus.getPieces()[1].get(i).getxCoordinate();
                        coordinate[1] = actualGameStatus.getPieces()[1].get(i).getyCoordinate();
                    }
                }
                if(!giantFound){
                    if(actualGameStatus.getPieces()[0].get(i).getPieceSymbol()=='d' &&
                            !GameModel.isSpecialCell(actualGameStatus.getPieces()[1].get(i).getxCoordinate(),(actualGameStatus.getPieces()[1].get(i).getyCoordinate()))){
                        coordinate[0] = actualGameStatus.getPieces()[1].get(i).getxCoordinate();
                        coordinate[1] = actualGameStatus.getPieces()[1].get(i).getyCoordinate();
                    }
                }
            }
        }
        return coordinate;
    }

    private static ArrayList<String> searchLostSpecialCell(GameModel actualGameStatus, String selectionOfMoves) {
        //NB: the game is passed with one turn already done, so w->b and b->w
        ArrayList<String> moves = new ArrayList<>();
        boolean upper = false;
        ArrayList<String> movesToBeChecked = new ArrayList<>();
        for(int i=0; i<selectionOfMoves.length()/5; i++){
            movesToBeChecked.add(selectionOfMoves.substring(i*5,(i+1)*5));
        }
        if(actualGameStatus.toString().charAt(0)=='W'){
            upper = true;
        }
        if(actualGameStatus.toString().charAt(0)=='B'){
            upper = false;
        }
        for(int i=0; i<SPECIAL_CELLS_RECORD.length; i++){
            for(int j=0; j<movesToBeChecked.size(); j++){
                int x = Character.getNumericValue(movesToBeChecked.get(j).charAt(4));
                int xT = SPECIAL_CELLS_RECORD[i][0]+1;
                int y =  Character.getNumericValue(movesToBeChecked.get(j).charAt(3));
                int yT = SPECIAL_CELLS_RECORD[i][1]+1;
                if(x ==  xT && y == yT){
                    if(!upper && isUpperCase(actualGameStatus.getCurrentConfiguration()[SPECIAL_CELLS_RECORD[i][0]][SPECIAL_CELLS_RECORD[i][1]])
                            && actualGameStatus.getCurrentConfiguration()[SPECIAL_CELLS_RECORD[i][0]][SPECIAL_CELLS_RECORD[i][1]]!='0'){
                        moves.add(movesToBeChecked.get(j));
                    }
                    if(upper && !isUpperCase(actualGameStatus.getCurrentConfiguration()[SPECIAL_CELLS_RECORD[i][0]][SPECIAL_CELLS_RECORD[i][1]])
                            && actualGameStatus.getCurrentConfiguration()[SPECIAL_CELLS_RECORD[i][0]][SPECIAL_CELLS_RECORD[i][1]]!='0'){
                        moves.add(movesToBeChecked.get(j));
                    }
                }
            }
        }
        return moves;
    }

    private static String selectBestNextMove(ArrayList<String> moves, GameModel game) {
        double maximumPoint = 0;
        int index = 0;
        int player = 0;
        if(game.toString().charAt(0) =='W'){
            player = 0;
        }else{
            if(game.toString().charAt(0)=='B'){
                player=1;
            }
        }
        ArrayList<String> movesToBeEvaluated  = new ArrayList<>();
        for(int i=0; i<moves.size(); i++){
            if(moves.get(i).length() >= 5*(recursionLevel +1)){
                movesToBeEvaluated.add(moves.get(i));
            }
        }
        for(int j=0; j<movesToBeEvaluated.size(); j++){
            maximumPoint=0;
            GameModel newGame = new GameModel();
            newGame = copyGameStatus(game);
            for(int k = 0; k<= recursionLevel; k++) {
                newGame.parseAction(movesToBeEvaluated.get(j).substring(k*5,(k+1)*5));
                newGame.repriseTurn();
                if(updatePlayersPoints(newGame)[player]>maximumPoint) {
                    maximumPoint = updatePlayersPoints(newGame)[player];
                    index = j;
                }
            }
        }

        return movesToBeEvaluated.get(index).substring(0,5)+Double.toString(maximumPoint);
        //for()
    }

    public static List<Object> calculateAllPossibleSituations(List<Object> gameDevelopment){
        boolean winnerFound = (boolean)gameDevelopment.get(4);
        if(winnerFound){
            return null;
        }else {
            int counter = (int) gameDevelopment.get(3);
            ArrayList<GameModel> actualGameSituations = (ArrayList<GameModel>) gameDevelopment.get(0);
            ArrayList<GameModel> newGameSituations = new ArrayList<>();
            ArrayList<String> actualMoveSituations = (ArrayList<String>) gameDevelopment.get(1);
            ArrayList<String> newMoveSituations = new ArrayList<>();
            ArrayList<Integer> actualRecursionReached = (ArrayList<Integer>) gameDevelopment.get(2);
            ArrayList<Integer> newRecursionLevel = new ArrayList<>();
            List<Object> newPossibleSituations = new ArrayList<>();

            for (int j = counter; j < actualGameSituations.size(); j++) {
                if (actualRecursionReached.get(j) > recursionLevel) {
                    return gameDevelopment;
                }else {
                    ArrayList<String> newMoves = setInterestingMoves(actualGameSituations.get(j),true);
                    //removeMovesThatRemovePiecesFromTheSpecialCells(newMoves,actualGameSituations.get(j));
                    for (int i = 0; i < newMoves.size(); i++) {
                        GameModel newGame = copyGameStatus(actualGameSituations.get(j));
                        newGame.parseAction(newMoves.get(i));
                        if (!newGame.toString().contains("ERROR")) {
                            newGame.repriseTurn();
                            newGameSituations.add(newGame);
                            newMoveSituations.add(actualMoveSituations.get(j) + newMoves.get(i));
                            newRecursionLevel.add(actualRecursionReached.get(j) + 1);
                            if (updatePlayersPoints(newGame)[0] > 500) {
                                List<Object> win = new ArrayList<>();
                                win.add("PLAYER W WIN");
                                win.add(actualMoveSituations.get(j) + newMoves.get(i));
                                gameDevelopment.set(4,true);
                                return win;
                            }
                            if (updatePlayersPoints(newGame)[1] > 500) {
                                List<Object> win = new ArrayList<>();
                                win.add("PLAYER B WIN");
                                win.add(actualMoveSituations.get(j) + newMoves.get(i));
                                gameDevelopment.set(4,true);
                                return win;
                            }
                        }

                    }
                    counter = j;
                }
            }
            newPossibleSituations.add(newGameSituations);
            newPossibleSituations.add(newMoveSituations);
            newPossibleSituations.add(newRecursionLevel);
            newPossibleSituations.add(counter);
            newPossibleSituations.add(false);
            return calculateAllPossibleSituations(newPossibleSituations);
        }
        //return gameDevelopment;
    }

    public static GameModel createGameFromString(String gameConfiguration) {
        GameModel gameModel = new GameModel();
        gameModel.initializePlayer("", 0, WHITE);
        gameModel.initializePlayer("", 1, BLACK);
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
            gameModel.setVitalities(gameConfiguration.substring(lastCharOfBoardConfiguration,
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

        return gameModel;
    }

    //This method is used to set the difficulty of the Artificial intelligence
    public static void setRecursionLevel(Integer recursionLevel) {
        ArtificialIntelligence.recursionLevel = recursionLevel;
    }
}
