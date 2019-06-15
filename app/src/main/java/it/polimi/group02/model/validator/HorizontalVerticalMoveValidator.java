package it.polimi.group02.model.validator;

import java.util.ArrayList;

import it.polimi.group02.model.Piece;
import it.polimi.group02.model.utility.MoveType;
import it.polimi.group02.model.utility.Utility;

import static it.polimi.group02.model.utility.Utility.BOARD_SIDE_SIZE;
import static java.lang.Math.abs;

/**
 * This class is used to calculate, having a starter position and a target position in the form of (x,y) coordinates,
 * if the target is reachable by the selected piece. This considers if there is some other piece that occupies some cells
 * on the walking, or flying path
 */
public class HorizontalVerticalMoveValidator extends ActionValidator implements MoveValidator {

    private boolean pathFoud;
    private String findedPath;
    private boolean startCombat;
    private ArrayList<String> allReachableCells;

    /**
     * The constructor recalls the one of the ActionValidator that this class extends
     * @param cells contains the game situation as matrix of chars
     * @param piece is the piece that is trying to move
     * @param startingX this is the x coordinate of the position from which we are trying to attack
     * @param startingY this is the y coordinate of the position from which we are trying to attack
     * @param targetX this is the target x position on which we want to send our attack
     * @param targetY this is the target y position on which we want to send our attack
     */
    public HorizontalVerticalMoveValidator(char[][] cells, Piece piece, int startingX, int startingY, int targetX, int targetY) {
        super(cells, piece, startingX, startingY, targetX, targetY);
    }


    /**
     * This method verifies if the player is allowed to move the piece in the selected position
     * the verification is made by first checking if the selected distance is covered by the piece moving range
     * the second step is to verify if there are no other pieces covering the path
     * I decided to make first the distance allowed verification because is less expensive in terms of complexity
     */
    public boolean checkMove() {

        //getting the range of the possible move
        int range = this.getPiece().getMoveRange().getValue();
        int startX = this.getStartingX();
        int startY = this.getStartingY();
        int destinationX = this.getTargetX();
        int destinationY = this.getTargetY();
        char[][] boardStatus = this.getCellSymbols();
        ArrayList<String> path = new ArrayList<>();
        // if the target cell is within the range and the starting cell is different from the target cell
        if(distanceAllowed(range)){
            //we can move over around other pieces but we can't fly on our piece
            if((this.getPiece().getMoveType().equals(MoveType.FLIGHT)) && !(belongToSamePlayer())){
                if(this.getCellSymbols()[this.getTargetX()][this.getTargetY()] != '0'){
                    this.setStartCombat(true);
                }else{
                    this.setStartCombat(false);
                }
                this.setPathFoud(true);
                path.add(Integer.toString(this.getPiece().getxCoordinate())+Integer.toString(this.getPiece().getyCoordinate())+";");
                path = checkFlyingPath(range, startX, startY, destinationX, destinationY, boardStatus, path, path.get(0));
                this.setAllReachableCells(path);
                return true;//if can fly we just need to be able to cover the distance
            }else{//if can't fly the path can't be covered with by other pieces
                ArrayList<String> findedPaths = new ArrayList<>();
                String shortestPath = null;
                this.setPathFoud(false);
                path.add(Integer.toString(this.getPiece().getxCoordinate())+Integer.toString(this.getPiece().getyCoordinate())+";");
                //if both conditions about allowed distance and no other pieces covering the path
                //if other piece is covering the path
                path = checkPathCoveredByOtherPieces(range, startX, startY, destinationX, destinationY, boardStatus, path, path.get(0));
                //this attribute will be useful when working with the interface, it will classic_show all the reachable cells
                this.setAllReachableCells(path);
                for(int i=0; i < path.size();i++){
                    int pathLength = path.get(i).length();
                    //saving the founded path
                    if ((Character.getNumericValue(path.get(i).charAt(pathLength-2))==destinationY)&&
                            (Character.getNumericValue(path.get(i).charAt(pathLength-3))==destinationX)){
                        //adding all the path founded
                        findedPaths.add(path.get(i));
                    }
                }
                //if there is some path we are setting the first one as the shortest
                if(findedPaths.size()>0) {
                    shortestPath = findedPaths.get(0);
                }
                //finding shortest path in the list of all the paths found
                for(int i=0; i<findedPaths.size(); i++){
                    if (findedPaths.get(i).length() < shortestPath.length()){
                        shortestPath = findedPaths.get(i);
                    }
                }
                this.setFindedPath(shortestPath);
                if(shortestPath != null) {
                    return true;
                }else{
                    return false;//if there is found path
                }
            }
        }else{
            return false;//if the distance is not allowed
        }
    }

    /**
     * This method calculates if the moving distance selected is allowed
     * For example if a piece that have range 2 wants to move from (1,2) to (2,3) is allowed
     * Instead if wants to move to (3,3) or (2,4) is not allowed
     * Plus, it checks if the starting cell and target cell aren't the same
     * @param distance this value is used as maximum distance in which a piece can move
     * @return a value that says us if the piece is allowed or not to move there for what concerns distance
     */
    public boolean distanceAllowed(int distance){
        if( ( (abs(getStartingX()-getTargetX())) + (abs(getStartingY()-getTargetY())) ) <= distance && !(getStartingX() == getTargetX() && getStartingY() == getTargetY())){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Recursive function that calls itself to find the next cell
     * @param distance used to know how many moves we can still make
     * @param startX starting x position
     * @param startY starting y position
     * @param destinationX destination x position
     * @param destinationY destination y position
     * @param boardStatus matrix of chars that contains the fantasy_board status
     * @param path is a string containing the path
     * @return an array list of all the paths found
     */
    private ArrayList<String> checkPathCoveredByOtherPieces(int distance, int startX, int startY, int destinationX, int destinationY, char[][] boardStatus, ArrayList<String> path, String actualPath) {
        //first we check if with the last move we reached the destination or if the maximum number of moves have been reached
        if((startX==destinationX)&&(startY==destinationY)){
            if(belongToSamePlayer()){
                this.setStartCombat(false);
            }else{
                if((this.getCellSymbols()[this.getTargetX()][this.getTargetY()]) != '0'){
                    this.setStartCombat(true);
                }else{
                    this.setStartCombat(false);
                }
            }
            this.setPathFoud(true);
            return path;
        }else{
            if(distance == 0){
                //it means we have not found any path
                return path;
            }else{
                if(isPathFoud()==false){
                    //getting the empty cells from the initial position
                    int[][] emptyCells = getEmptyCellsNearActualPosition(boardStatus, startX, startY, destinationX, destinationY);
                    if (emptyCells[0][0] != -1) {//-1 indicates there are no empty cells
                        for(int i = 0; i < emptyCells.length; i++) {
                            //we are creating a new path from the last point reached
                            //diving per 3 because the path string si composed as substrings like xy; so 3 characters
                            //we can add a new cell only if we are not reaching the maximum distance the piece can move
                            if(((actualPath.length()/3)-1 < this.getPiece().getMoveRange().getValue()) && (emptyCells[i][0]!=-1)){
                                path.add(actualPath+(Integer.toString(emptyCells[i][0])+Integer.toString(emptyCells[i][1])+ ";"));//adding x and y coordinate to the path string
                            }
                            //we used the -1 value as not allowed
                            if((emptyCells[i][0]!=-1)){//looking for new paths
                                checkPathCoveredByOtherPieces(distance-1,emptyCells[i][0],emptyCells[i][1],destinationX,destinationY,boardStatus,path,path.get(path.size()-1));
                            }
                        }
                    } else {//if there are no empty cells the moved can't be performed
                        return path;
                    }
                }
            }
        }
        return path;
    }

    private ArrayList<String> checkFlyingPath(int distance, int startX, int startY, int destinationX, int destinationY, char[][] boardStatus, ArrayList<String> path, String actualPath) {
        if(destinationX>startX) {
            for (int i = 1; i <= distance && startX + i < Utility.BOARD_SIDE_SIZE; i++) {
                if (boardStatus[startX + i][startY] == '0' || (Character.isUpperCase(boardStatus[startX][startY]) && !Character.isUpperCase(boardStatus[startX + i][startY])) ||
                        (!Character.isUpperCase(boardStatus[startX][startY]) && Character.isUpperCase(boardStatus[startX + i][startY]))) {
                    actualPath = actualPath + Integer.toString(startX + i) + Integer.toString(startY) + ";";
                }
                if (startX + i == destinationX) {
                    if (startY == destinationY) {
                        path.add(actualPath);
                    } else {
                        if (destinationY > startY) {
                            for (int j = 1; j <= distance - (i - 1) && startY + j < Utility.BOARD_SIDE_SIZE; j++) {
                                if (boardStatus[destinationX][startY + j] == '0' || (Character.isUpperCase(boardStatus[startX][startY]) && !Character.isUpperCase(boardStatus[destinationX][startY + j])) ||
                                        (!Character.isUpperCase(boardStatus[startX][startY]) && Character.isUpperCase(boardStatus[destinationX][startY + j]))) {
                                    actualPath = actualPath + Integer.toString(destinationX) + Integer.toString(startY + j) + ";";
                                }
                                if (startY + j == destinationY) {
                                    path.add(actualPath);
                                }
                            }
                        } else if (destinationY < startY) {
                            for (int k = 1; k <= distance - (i - 1) && startY - k >= 0; k++) {
                                if (boardStatus[destinationX][startY - k] == '0' || (Character.isUpperCase(boardStatus[startX][startY]) && !Character.isUpperCase(boardStatus[destinationX][startY - k])) ||
                                        (!Character.isUpperCase(boardStatus[startX][startY]) && Character.isUpperCase(boardStatus[destinationX][startY - k]))) {
                                    actualPath = actualPath + Integer.toString(destinationX) + Integer.toString(startY - k) + ";";
                                }
                                if (startY - k == destinationY) {
                                    path.add(actualPath);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (destinationX < startX) {
            for (int i = 1; i <= distance && startX - i >= 0; i++) {
                if (boardStatus[startX - i][startY] == '0' || (Character.isUpperCase(boardStatus[startX][startY]) && !Character.isUpperCase(boardStatus[startX - i][startY])) ||
                        (!Character.isUpperCase(boardStatus[startX][startY]) && Character.isUpperCase(boardStatus[startX - i][startY]))) {
                    actualPath = actualPath + Integer.toString(startX - i) + Integer.toString(startY) + ";";
                }
                if (startX - i == destinationX) {
                    if (startY == destinationY) {
                        path.add(actualPath);
                    } else {
                        if (destinationY > startY) {
                            for (int j = 1; j <= distance - (i - 1) && startY + j < Utility.BOARD_SIDE_SIZE; j++) {
                                if (boardStatus[destinationX][startY + j] == '0' || (Character.isUpperCase(boardStatus[startX][startY]) && !Character.isUpperCase(boardStatus[destinationX][startY + j])) ||
                                        (!Character.isUpperCase(boardStatus[startX][startY]) && Character.isUpperCase(boardStatus[destinationX][startY + j]))) {
                                    actualPath = actualPath + Integer.toString(destinationX) + Integer.toString(startY + j) + ";";
                                }
                                if (startY + j == destinationY) {

                                    path.add(actualPath);
                                }
                            }
                        } else if (destinationY < startY) {
                            for (int k = 1; k <= distance - (i - 1) && startY - k >= 0; k++) {
                                if (boardStatus[destinationX][startY - k] == '0' || (Character.isUpperCase(boardStatus[startX][startY]) && !Character.isUpperCase(boardStatus[destinationX][startY - k])) ||
                                        (!Character.isUpperCase(boardStatus[startX][startY]) && Character.isUpperCase(boardStatus[destinationX][startY - k]))) {
                                    actualPath = actualPath + Integer.toString(destinationX) + Integer.toString(startY - k) + ";";
                                }
                                if (startY - k == destinationY) {

                                    path.add(actualPath);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (destinationX == startX) {

            if (startY == destinationY) {
                actualPath = actualPath + Integer.toString(startX) + Integer.toString(startY) + ";";
                path.add(actualPath);
            } else {
                if (destinationY > startY) {
                    for (int j = 1; j <= distance && startY + j < Utility.BOARD_SIDE_SIZE; j++) {
                        if (boardStatus[destinationX][startY + j] == '0' || (Character.isUpperCase(boardStatus[startX][startY]) && !Character.isUpperCase(boardStatus[destinationX][startY + j])) ||
                                (!Character.isUpperCase(boardStatus[startX][startY]) && Character.isUpperCase(boardStatus[destinationX][startY + j]))) {
                            actualPath = actualPath + Integer.toString(destinationX) + Integer.toString(startY + j) + ";";
                        }
                        if (startY + j == destinationY) {
                            actualPath = actualPath + Integer.toString(startX) + Integer.toString(startY) + ";";
                            path.add(actualPath);
                        }
                    }
                } else if (destinationY < startY) {
                    for (int k = 1; k <= distance && startY - k >= 0; k++) {
                        if (boardStatus[destinationX][startY - k] == '0' || (Character.isUpperCase(boardStatus[startX][startY]) && !Character.isUpperCase(boardStatus[destinationX][startY - k])) ||
                                (!Character.isUpperCase(boardStatus[startX][startY]) && Character.isUpperCase(boardStatus[destinationX][startY - k]))) {
                            actualPath = actualPath + Integer.toString(destinationX) + Integer.toString(startY - k) + ";";
                        }
                        if (startY - k == destinationY) {
                            actualPath = actualPath + Integer.toString(startX) + Integer.toString(startY) + ";";
                            path.add(actualPath);
                        }
                    }
                }
            }

        }

        return path;
    }

    /**
     * This method checks from an initial position which are free
     * as free positions are allowed also cell in which there is some piece
     * but is the destination, so this will start a combat
     * @param board a matrix of chars containing the fantasy_board status
     * @param x cell from which we are looking around
     * @param y cell from which we are looking around
     * @param destX cell destination x
     * @param destY cell destination y
     * @return returning a matrix with the coordinates of empty cells
     */
    public int[][] getEmptyCellsNearActualPosition(char[][] board, int x, int y, int destX, int destY){
        //at the maximum we can have 4 free near horizontal or vertical cells
        //I preferred using an array instead a list because even if the array occupies more
        //space it reduces the computational complexity
        int[][] emptyCells = new int[4][2];
        //this value is used as negative to recognize that no empty cells are available
        emptyCells[0][0] = -1;
        //used also to count how many cells we found
        int numberOfEmptyCells = 0;
        //checking all the moves near the starting cell
        if((x+1<BOARD_SIDE_SIZE) && ((board[x+1][y] == '0') || (((x+1) == destX) && (y == destY))) &&
                !belongToSamePlayer()){
            emptyCells[numberOfEmptyCells][0] = (x+1);
            emptyCells[numberOfEmptyCells][1] = y;
            numberOfEmptyCells++;
        }
        if((x-1>=0) && ((board[x-1][y] == '0') || (((x-1) == destX) && (y == destY))) &&
                !belongToSamePlayer()){
            emptyCells[numberOfEmptyCells][0] = (x-1);
            emptyCells[numberOfEmptyCells][1] = y;
            numberOfEmptyCells++;
        }
        if((y+1<BOARD_SIDE_SIZE) && ((board[x][y+1] == '0') || ((x == destX) && ((y+1) == destY))) &&
                !belongToSamePlayer()){
            emptyCells[numberOfEmptyCells][0] = x;
            emptyCells[numberOfEmptyCells][1] = y+1;
            numberOfEmptyCells++;
        }
        if((y-1>=0) && (board[x][y-1] == '0' || ((x == destX) && ((y-1) == destY))) &&
                !belongToSamePlayer()) {
            emptyCells[numberOfEmptyCells][0] = x;
            emptyCells[numberOfEmptyCells][1] = y-1;
            numberOfEmptyCells++;
        }
        if(numberOfEmptyCells < 4){
            for(int i=numberOfEmptyCells; i<4; i++){
                emptyCells[i][0] = -1;
            }
        }
        return emptyCells;
    }

    /**
     * This class is used to check if 2 pieces belong to the same player
     * @return true if are from the same player, false otherwise
     */
    public boolean belongToSamePlayer() {
        boolean bothUpperCase = Character.isUpperCase(this.getCellSymbols()[this.getTargetX()][this.getTargetY()]) && Character.isUpperCase(this.getPiece().getPieceSymbol());
        boolean bothLowerCase = Character.isLowerCase(this.getCellSymbols()[this.getTargetX()][this.getTargetY()]) && Character.isLowerCase(this.getPiece().getPieceSymbol());

        return (bothUpperCase || bothLowerCase);
    }



    public void setFindedPath(String findedPath) {
        this.findedPath = findedPath;
    }


    public boolean isStartCombat() {
        return startCombat;
    }


    public void setStartCombat(boolean startCombat) {
        this.startCombat = startCombat;
    }


    public boolean isPathFoud() {
        return pathFoud;
    }


    public void setPathFoud(boolean pathFoud) {
        this.pathFoud = pathFoud;
    }

    public ArrayList<String> getAllReachableCells() {
        return allReachableCells;
    }

    public void setAllReachableCells(ArrayList<String> allReachableCells) {
        this.allReachableCells = allReachableCells;
    }
}