package it.polimi.group02.model.validator;

import java.util.ArrayList;

import it.polimi.group02.model.Piece;
import it.polimi.group02.model.utility.Direction;
import it.polimi.group02.model.utility.Utility;

/**
 * This class will be useful when the graphical part will be implemented. It allows to highlight which all the reachable
 * cells form the position of the selected piece. It is different from the move validator because it checks all the
 * possible destinations while the move validator needs a target to check if it is reachable.
 */
public class CalculateReachableCells {

    /**
     * This method calculates all the reachable cells without the need to specify a target
     * NB: This method was not inserted in the HorizontalVerticalMoveValidator class because if not it should be modified the design
     * and even because it's a different point of view, the validator has to validate a specific path, this method uses the validator
     * to find all the possible paths. It is static because there is no requirement of instantiating a new object, we can simply call the method
     * @return a list of string of the founded paths
     */
    public static ArrayList<String> calculateAllReachableCellsWithouthTarget(Piece p, char[][] cells) {
        boolean northFlag = false;
        boolean southFlag = false;
        boolean estFlag = false;
        boolean westFlag = false;
        boolean northEstFlag = false;
        boolean southEstFlag = false;
        boolean northWestFlag = false;
        boolean southWestFlag = false;
        Direction moveDirection = p.getMoveDirection();
        ArrayList<String> paths = new ArrayList<>();
        ArrayList<String> reachableCells = new ArrayList<>();
        HorizontalVerticalMoveValidator hvmv;
        AnyMoveValidator anymv;
        String newCell;
        int rangeOfMove = p.getMoveRange().getValue();
        int pX = p.getxCoordinate();
        int pY = p.getyCoordinate();
        //The horizontal vertical case is treated considering the 4 cardinal points (north,south,est,west)
        //and after that creating a sort of rhombus and exploring the path on the sides of the rhombus
        if (moveDirection.toString().equals("HORIZONTAL_VERTICAL")) {
            if ((pX + rangeOfMove) < Utility.BOARD_SIDE_SIZE) {
                hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, (pX + rangeOfMove), pY);
                hvmv.checkMove();
                if (hvmv.getAllReachableCells() != null)
                    paths.addAll(hvmv.getAllReachableCells());
                estFlag = true;
            }
            if ((pY + rangeOfMove) < Utility.BOARD_SIDE_SIZE) {
                hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, pX, (pY + rangeOfMove));
                hvmv.checkMove();
                if (hvmv.getAllReachableCells() != null)
                    paths.addAll(hvmv.getAllReachableCells());
                northFlag = true;
            }
            if ((pX - rangeOfMove) >= 0) {
                hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, (pX - rangeOfMove), pY);
                hvmv.checkMove();
                if (hvmv.getAllReachableCells() != null)
                    paths.addAll(hvmv.getAllReachableCells());
                westFlag = true;
            }
            if ((pY - rangeOfMove) >= 0) {
                hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, pX, (pY - rangeOfMove));
                hvmv.checkMove();
                if (hvmv.getAllReachableCells() != null)
                    paths.addAll(hvmv.getAllReachableCells());
                southFlag = true;
            }
            //diagonal verification from est to north
            if (estFlag || northFlag) {//check if at least one of the extreme does not fall out from the fantasy_board
                if (estFlag && northFlag) {//if both are inside
                    for (int i = (pX + rangeOfMove), j = pY; i >= pX && j <= (pY + rangeOfMove); j++, i--) {
                        hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, i, j);
                        hvmv.checkMove();
                        if (hvmv.getAllReachableCells() != null)
                            paths.addAll(hvmv.getAllReachableCells());
                    }
                } else {
                    if (estFlag) {//if only the est flag, we have to stop at the fantasy_board
                        for (int i = (pX + rangeOfMove), j = pY; i >= pX && j < Utility.BOARD_SIDE_SIZE; j++, i--) {
                            hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, i, j);
                            hvmv.checkMove();
                            if (hvmv.getAllReachableCells() != null)
                                paths.addAll(hvmv.getAllReachableCells());
                        }
                    } else {//if only the north flag
                        for (int i = Utility.BOARD_SIDE_SIZE - 1, j = pY; i >= pX && j <= (pY + rangeOfMove); j++, i--) {
                            hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, i, j);
                            hvmv.checkMove();
                            if (hvmv.getAllReachableCells() != null)
                                paths.addAll(hvmv.getAllReachableCells());
                        }
                    }
                }
            }

            //diagonal verification from west to north
            if (westFlag || northFlag) {//check if at least one of the extreme does not fall out from the fantasy_board
                if (westFlag && northFlag) {//if both are inside
                    for (int i = (pX - rangeOfMove), j = pY; i <= pX && j <= (pY + rangeOfMove); j++, i++) {
                        hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, i, j);
                        hvmv.checkMove();
                        if (hvmv.getAllReachableCells() != null)
                            paths.addAll(hvmv.getAllReachableCells());
                    }
                } else {
                    if (westFlag) {//if only the west flag, we have to stop at the fantasy_board
                        for (int i = (pX - rangeOfMove), j = pY; i <= pX && j < Utility.BOARD_SIDE_SIZE; j++, i++) {
                            hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, i, j);
                            hvmv.checkMove();
                            if (hvmv.getAllReachableCells() != null)
                                paths.addAll(hvmv.getAllReachableCells());
                        }
                    } else {//if only the north flag
                        for (int i = 0, j = pY; i <= pX && j <= (pY + rangeOfMove); j++, i++) {
                            hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, i, j);
                            hvmv.checkMove();
                            if (hvmv.getAllReachableCells() != null)
                                paths.addAll(hvmv.getAllReachableCells());
                        }
                    }
                }
            }

            //diagonal verification from west to south
            if (westFlag || southFlag) {//check if at least one of the extreme does not fall out from the fantasy_board
                if (westFlag && southFlag) {//if both are inside
                    for (int i = (pX - rangeOfMove), j = pY; i <= pX && j >= (pY - rangeOfMove); j--, i++) {
                        hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, i, j);
                        hvmv.checkMove();
                        if (hvmv.getAllReachableCells() != null)
                            paths.addAll(hvmv.getAllReachableCells());
                    }
                } else {
                    if (westFlag) {//if only the west flag, we have to stop at the fantasy_board
                        for (int i = (pX - rangeOfMove), j = pY; i <= pX && j >= 0; j--, i++) {
                            hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, i, j);
                            hvmv.checkMove();
                            if (hvmv.getAllReachableCells() != null)
                                paths.addAll(hvmv.getAllReachableCells());
                        }
                    } else {//if only the south flag
                        for (int i = 0, j = pY; i <= pX && j >= (pY - rangeOfMove); j--, i++) {
                            hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, i, j);
                            hvmv.checkMove();
                            if (hvmv.getAllReachableCells() != null)
                                paths.addAll(hvmv.getAllReachableCells());
                        }
                    }
                }
            }

            //diagonal verification from west to north
            if (estFlag || southFlag) {//check if at least one of the extreme does not fall out from the fantasy_board
                if (estFlag && southFlag) {//if both are inside
                    for (int i = (pX + rangeOfMove), j = pY; i >= pX && j >= (pY - rangeOfMove); j--, i--) {
                        hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, i, j);
                        hvmv.checkMove();
                        if (hvmv.getAllReachableCells() != null)
                            paths.addAll(hvmv.getAllReachableCells());
                    }
                } else {
                    if (estFlag) {//if only the west flag, we have to stop at the fantasy_board
                        for (int i = (pX + rangeOfMove), j = pY; i >= pX && j >= 0; j--, i--) {
                            hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, i, j);
                            hvmv.checkMove();
                            if (hvmv.getAllReachableCells() != null)
                                paths.addAll(hvmv.getAllReachableCells());
                        }
                    } else {//if only the north flag
                        for (int i = Utility.BOARD_SIDE_SIZE - 1, j = pY; i >= pX && j >= (pY - rangeOfMove); j--, i--) {
                            hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, i, j);
                            hvmv.checkMove();
                            if (hvmv.getAllReachableCells() != null)
                                paths.addAll(hvmv.getAllReachableCells());
                        }
                    }
                }
            }

            if(!((estFlag || northFlag) && (westFlag || northFlag) && (estFlag || southFlag) && (westFlag || southFlag))){
                for (int k = 0; k < Utility.BOARD_SIDE_SIZE; k++) {
                    for (int h = 0; h < Utility.BOARD_SIDE_SIZE; h++) {
                        hvmv = new HorizontalVerticalMoveValidator(cells, p, pX, pY, k, h);
                        hvmv.checkMove();
                        if (hvmv.getAllReachableCells() != null)
                            paths.addAll(hvmv.getAllReachableCells());
                    }
                }
            }

        } else {//the any case instead is made on a square build with the nw, ne, sw, se flags
            if ((pX + rangeOfMove) < Utility.BOARD_SIDE_SIZE && (pY + rangeOfMove) < Utility.BOARD_SIDE_SIZE) {
                anymv = new AnyMoveValidator(cells, p, pX, pY, (pX + rangeOfMove), (pY + rangeOfMove));
                anymv.checkMove();
                if (anymv.getAllReachableCells() != null)
                    paths.addAll(anymv.getAllReachableCells());
                northEstFlag = true;
            }
            if ((pX - rangeOfMove) >= 0 && (pY + rangeOfMove) < Utility.BOARD_SIDE_SIZE) {
                anymv = new AnyMoveValidator(cells, p, pX, pY, (pX - rangeOfMove), (pY + rangeOfMove));
                anymv.checkMove();
                if (anymv.getAllReachableCells() != null)
                    paths.addAll(anymv.getAllReachableCells());
                northWestFlag = true;
            }
            if ((pX + rangeOfMove) < Utility.BOARD_SIDE_SIZE && (pY - rangeOfMove) >= 0) {
                anymv = new AnyMoveValidator(cells, p, pX, pY, (pX + rangeOfMove), (pY - rangeOfMove));
                anymv.checkMove();
                if (anymv.getAllReachableCells() != null)
                    paths.addAll(anymv.getAllReachableCells());
                southEstFlag = true;
            }
            if ((pX - rangeOfMove) >= 0 && (pY - rangeOfMove) >= 0) {
                anymv = new AnyMoveValidator(cells, p, pX, pY, (pX - rangeOfMove), (pY - rangeOfMove));
                anymv.checkMove();
                if (anymv.getAllReachableCells() != null)
                    paths.addAll(anymv.getAllReachableCells());
                southWestFlag = true;
            }

            //vertical verification from northEst to southEst
            if (northEstFlag || southEstFlag) {//check if at least one of the extreme does not fall out from the fantasy_board
                if (northEstFlag && southEstFlag) {//if both are inside
                    for (int i = (pX + rangeOfMove), j = (pY + rangeOfMove); j >= (pY - rangeOfMove); j--) {
                        anymv = new AnyMoveValidator(cells, p, pX, pY, i, j);
                        anymv.checkMove();
                        if (anymv.getAllReachableCells() != null)
                            paths.addAll(anymv.getAllReachableCells());
                    }
                } else {
                    if (northEstFlag) {//if only the west flag, we have to stop at the fantasy_board
                        for (int i = (pX + rangeOfMove), j = (pY + rangeOfMove); j >= 0; j--) {
                            anymv = new AnyMoveValidator(cells, p, pX, pY, i, j);
                            anymv.checkMove();
                            if (anymv.getAllReachableCells() != null)
                                paths.addAll(anymv.getAllReachableCells());
                        }
                    } else {//if only the north flag
                        for (int i = (pX + rangeOfMove), j = Utility.BOARD_SIDE_SIZE - 1; j >= (pY - rangeOfMove); j--) {
                            anymv = new AnyMoveValidator(cells, p, pX, pY, i, j);
                            anymv.checkMove();
                            if (anymv.getAllReachableCells() != null)
                                paths.addAll(anymv.getAllReachableCells());
                        }
                    }
                }
            }

            //vertical verification from northWest to southEst
            if (northWestFlag || southWestFlag) {//check if at least one of the extreme does not fall out from the fantasy_board
                if (northWestFlag && southWestFlag) {//if both are inside
                    for (int i = (pX - rangeOfMove), j = (pY + rangeOfMove); j >= (pY - rangeOfMove); j--) {
                        anymv = new AnyMoveValidator(cells, p, pX, pY, i, j);
                        anymv.checkMove();
                        if (anymv.getAllReachableCells() != null)
                            paths.addAll(anymv.getAllReachableCells());
                    }
                } else {
                    if (northWestFlag) {//if only the west flag, we have to stop at the fantasy_board
                        for (int i = (pX - rangeOfMove), j = (pY + rangeOfMove); j >= 0; j--) {
                            anymv = new AnyMoveValidator(cells, p, pX, pY, i, j);
                            anymv.checkMove();
                            if (anymv.getAllReachableCells() != null)
                                paths.addAll(anymv.getAllReachableCells());
                        }
                    } else {//if only the north flag
                        for (int i = (pX - rangeOfMove), j = Utility.BOARD_SIDE_SIZE - 1; j >= (pY - rangeOfMove); j--) {
                            anymv = new AnyMoveValidator(cells, p, pX, pY, i, j);
                            anymv.checkMove();
                            if (anymv.getAllReachableCells() != null)
                                paths.addAll(anymv.getAllReachableCells());
                        }
                    }
                }
            }

            //horizontal verification from southwest to southEst
            if (southWestFlag || southEstFlag) {//check if at least one of the extreme does not fall out from the fantasy_board
                if (southWestFlag && southEstFlag) {//if both are inside
                    for (int i = (pX - rangeOfMove), j = (pY - rangeOfMove); i <= (pX + rangeOfMove); i++) {
                        anymv = new AnyMoveValidator(cells, p, pX, pY, i, j);
                        anymv.checkMove();
                        if (anymv.getAllReachableCells() != null)
                            paths.addAll(anymv.getAllReachableCells());
                    }
                } else {
                    if (southWestFlag) {//if only the west flag, we have to stop at the fantasy_board
                        for (int i = (pX - rangeOfMove), j = (pY - rangeOfMove); i < Utility.BOARD_SIDE_SIZE; i++) {
                            anymv = new AnyMoveValidator(cells, p, pX, pY, i, j);
                            anymv.checkMove();
                            if (anymv.getAllReachableCells() != null)
                                paths.addAll(anymv.getAllReachableCells());
                        }
                    } else {//if only the north flag
                        for (int i = 0, j = (pY - rangeOfMove); i <= (pX + rangeOfMove); i++) {
                            anymv = new AnyMoveValidator(cells, p, pX, pY, i, j);
                            anymv.checkMove();
                            if (anymv.getAllReachableCells() != null)
                                paths.addAll(anymv.getAllReachableCells());
                        }
                    }
                }
            }

            //horizontal verification from northwest to northEst
            if (northWestFlag || northEstFlag) {//check if at least one of the extreme does not fall out from the fantasy_board
                if (northWestFlag && northEstFlag) {//if both are inside
                    for (int i = (pX - rangeOfMove), j = (pY + rangeOfMove); i <= (pX + rangeOfMove); i++) {
                        anymv = new AnyMoveValidator(cells, p, pX, pY, i, j);
                        anymv.checkMove();
                        if (anymv.getAllReachableCells() != null)
                            paths.addAll(anymv.getAllReachableCells());
                    }
                } else {
                    if (northWestFlag) {//if only the west flag, we have to stop at the fantasy_board
                        for (int i = (pX - rangeOfMove), j = (pY + rangeOfMove); i < Utility.BOARD_SIDE_SIZE; i++) {
                            anymv = new AnyMoveValidator(cells, p, pX, pY, i, j);
                            anymv.checkMove();
                            if (anymv.getAllReachableCells() != null)
                                paths.addAll(anymv.getAllReachableCells());
                        }
                    } else {//if only the north flag
                        for (int i = 0, j = (pY + rangeOfMove); i <= (pX + rangeOfMove); i++) {
                            anymv = new AnyMoveValidator(cells, p, pX, pY, i, j);
                            anymv.checkMove();
                            if (anymv.getAllReachableCells() != null)
                                paths.addAll(anymv.getAllReachableCells());
                        }
                    }
                }
            }

            if(!((northEstFlag || southEstFlag) && (northEstFlag || northWestFlag) && (northWestFlag || southWestFlag) && (southEstFlag || southWestFlag))){
                for (int k = 0; k < Utility.BOARD_SIDE_SIZE; k++) {
                    for (int h = 0; h < Utility.BOARD_SIDE_SIZE; h++) {
                        anymv = new AnyMoveValidator(cells, p, pX, pY, k, h);
                        anymv.checkMove();
                        if (anymv.getAllReachableCells() != null)
                            paths.addAll(anymv.getAllReachableCells());
                    }
                }
            }
        }

        //getting the single cells that are reachable from all the paths found
        for(int i=0; i<paths.size(); i++){
            for(int j=0; j<paths.get(i).length(); j=j+3) {
                newCell = String.valueOf(paths.get(i).charAt(j));
                newCell = newCell + String.valueOf(paths.get(i).charAt(j+1));
                if(!reachableCells.contains(newCell)){
                    reachableCells.add(newCell);
                }
            }
        }

        return reachableCells;
    }

}
