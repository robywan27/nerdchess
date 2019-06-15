package it.polimi.group02.model.validator;
import it.polimi.group02.model.Piece;

import static it.polimi.group02.model.utility.Utility.BOARD_SIDE_SIZE;
import static java.lang.Math.abs;

/**
 * This class is an extension of the HorizontalVerticalMoveValidator because the algorithm of searching the possible paths
 * is almost the same, we had only to introduce, by overriding the method getEmptyCellsNearActualPosition, the checking of
 * the diagonal directions to see if are free
 */
public class AnyMoveValidator extends HorizontalVerticalMoveValidator implements MoveValidator {
    public AnyMoveValidator(char[][] cells, Piece piece, int startingX, int startingY, int targetX, int targetY) {
        super(cells, piece, startingX, startingY, targetX, targetY);
    }


    /**
     * This is the version of the distance calculator for the Any Direction
     * It overrides the same method of the extended class. The calculation is done by considering also
     * the diagonal possibility of movement
     * Plus, it checks if the starting cell and target cell aren't the same
     * @param distance the value that tells us the maximum distance reachable by performing any kind of move
     * @return true value if we are allowed to perfrom this kind of move
     */
    @Override
    public boolean distanceAllowed(int distance){
        if( ( ((abs(getStartingX()-getTargetX())) <= distance) &&  ((abs(getStartingY()-getTargetY())) ) <= distance)  && !(getStartingX() == getTargetX() && getStartingY() == getTargetY()) ){
            return true;
        }else{
            return false;
        }
    }

    /**
     * This method overrides the same one in the horizontalVerticalMove class. It simply introduces 4 more options
     * when checking the empty cells, regarding the diagonal directions from the actual one
     * @param board a matrix of chars containing the fantasy_board status
     * @param x cell from which we are looking around
     * @param y cell from which we are looking around
     * @param destX cell destination x
     * @param destY cell destination y
     * @return an array of int representing the valid cells for a move
     */
    @Override
    public int[][] getEmptyCellsNearActualPosition(char[][] board, int x, int y, int destX, int destY){
        boolean bothUpperCase = Character.isUpperCase(this.getCellSymbols()[this.getTargetX()][this.getTargetY()]) && Character.isUpperCase(this.getPiece().getPieceSymbol());
        boolean bothLowerCase = Character.isLowerCase(this.getCellSymbols()[this.getTargetX()][this.getTargetY()]) && Character.isLowerCase(this.getPiece().getPieceSymbol());
        //at the maximum we can have 4 free near horizontal or vertical cells
        //I preferred using an array instead a list because even if the array occupies more
        //space it reduces the computational complexity
        int[][] emptyCells = new int[8][2];
        //this value is used as negative to recognize that no empty cells are available
        emptyCells[0][0] = -1;
        //used also to count how many cells we found
        int numberOfEmptyCells = 0;
        //checking all the moves near the starting cell
        if((x+1<BOARD_SIDE_SIZE) && ((board[x+1][y] == '0') || (((x+1) == destX) && (y == destY))) &&
                !(bothUpperCase || bothLowerCase)){
            emptyCells[numberOfEmptyCells][0] = (x+1);
            emptyCells[numberOfEmptyCells][1] = y;
            numberOfEmptyCells++;
        }
        if((x-1>=0) && ((board[x-1][y] == '0') || (((x-1) == destX) && (y == destY))) &&
                !(bothUpperCase || bothLowerCase)){
            emptyCells[numberOfEmptyCells][0] = (x-1);
            emptyCells[numberOfEmptyCells][1] = y;
            numberOfEmptyCells++;
        }
        if((y+1<BOARD_SIDE_SIZE) && ((board[x][y+1] == '0') || ((x == destX) && ((y+1) == destY))) &&
                !(bothUpperCase || bothLowerCase)){
            emptyCells[numberOfEmptyCells][0] = x;
            emptyCells[numberOfEmptyCells][1] = y+1;
            numberOfEmptyCells++;
        }
        if((y-1>=0) && (board[x][y-1] == '0' || ((x == destX) && ((y-1) == destY))) &&
                !(bothUpperCase || bothLowerCase)) {
            emptyCells[numberOfEmptyCells][0] = x;
            emptyCells[numberOfEmptyCells][1] = y-1;
            numberOfEmptyCells++;
        }
        if((y-1>=0) && (x-1>=0) && (board[x-1][y-1] == '0' || (((x-1) == destX) && ((y-1) == destY))) &&
                !(bothUpperCase || bothLowerCase)) {
            emptyCells[numberOfEmptyCells][0] = x-1;
            emptyCells[numberOfEmptyCells][1] = y-1;
            numberOfEmptyCells++;
        }
        if((y-1>=0) && (x+1<BOARD_SIDE_SIZE) && (board[x+1][y-1] == '0' || (((x+1) == destX) && ((y-1) == destY))) &&
                !(bothUpperCase || bothLowerCase)) {
            emptyCells[numberOfEmptyCells][0] = x+1;
            emptyCells[numberOfEmptyCells][1] = y-1;
            numberOfEmptyCells++;
        }
        if((y+1<BOARD_SIDE_SIZE) && (x+1<BOARD_SIDE_SIZE) && (board[x+1][y+1] == '0' || (((x+1) == destX) && ((y+1) == destY))) &&
                !(bothUpperCase || bothLowerCase)) {
            emptyCells[numberOfEmptyCells][0] = x+1;
            emptyCells[numberOfEmptyCells][1] = y+1;
            numberOfEmptyCells++;
        }
        if((y+1<BOARD_SIDE_SIZE) && (x-1>=0) && (board[x-1][y+1] == '0' || (((x-1) == destX) && ((y+1) == destY))) &&
                !(bothUpperCase || bothLowerCase)) {
            emptyCells[numberOfEmptyCells][0] = x-1;
            emptyCells[numberOfEmptyCells][1] = y+1;
            numberOfEmptyCells++;
        }
        if(numberOfEmptyCells < 8){
            for(int i=numberOfEmptyCells; i<8; i++){
                emptyCells[i][0] = -1;
            }
        }
        return emptyCells;
    }
}