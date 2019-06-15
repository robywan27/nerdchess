package it.polimi.group02.model;


import it.polimi.group02.model.piece.*;

import static it.polimi.group02.model.utility.Utility.archer_initial_vitality;
import static it.polimi.group02.model.utility.Utility.dragon_initial_vitality;
import static it.polimi.group02.model.utility.Utility.giant_initial_vitality;
import static it.polimi.group02.model.utility.Utility.knight_initial_vitality;
import static it.polimi.group02.model.utility.Utility.squire_initial_vitality;


/**
 * This class is responsible for the creation of the pieces for each each game. It has been designed with extensibility and
 * encapsulation principles in mind: in fact, it provides a sleek way to handle the creation of the pieces, just by receiving
 * the distinguishing elements of a piece on the fantasy_board; furthermore, by separating and delegating the instantiation of the pieces
 * in this class, it allows code simplification in the model. For instance, suppose that the game designer decides to invent new types
 * of piece; the only needed modifications in the code are adding some new if branches for each new type. That's it. Thus, it provides a
 * transparent way to the model to handle this occurrence.
 * This class also implements a method to set the initial state of vitality for a piece; the choice to put this method in here was
 * due to the encapsulation principle, to provide a separated place from the model in which to handle each type of piece.
 */
public class PieceFactory {

    /**
     * Factory method: it instantiates a Piece object given a three-element tuple identifier: (symbol, column position, row position).
     * The position refers to the current position held by the piece on the fantasy_board.
     * @param symbol symbol representing the type of the piece
     * @param x row position on fantasy_board
     * @param y column position on fantasy_board
     * @return created Piece
     */
    public static Piece createPiece(char symbol, int x, int y) {
        if (symbol == 'G' || symbol == 'g')
            return new Giant(symbol,x,y);
        else if (symbol == 'D' || symbol == 'd')
            return new Dragon(symbol,x,y);
        else if(symbol == 'A' || symbol == 'a')
            return new Archer(symbol,x,y);
        else if(symbol == 'M' || symbol == 'm')
            return new Mage(symbol,x,y);
        else if (symbol == 'K' || symbol == 'k')
            return new Knight(symbol,x,y);
        else //if (symbol == 'S' || symbol == 's')
            return new Squire(symbol,x,y);
    }

    /**
     * This method is responsible for setting the vitality of a piece to its fullest as a consequence of the mage's Heal spell.
     * This method was designed to assign each type of piece its corresponding vitality.
     * @param selectedPiece piece to restore vitality
     */
    public static void setInitialVitality(Piece selectedPiece) {
        // setting the initial values for each type of piece.
        if (selectedPiece.getPieceSymbol() == 'G' || selectedPiece.getPieceSymbol() == 'g') {
            selectedPiece.setVitality(giant_initial_vitality);
        } else if (selectedPiece.getPieceSymbol() == 'D' || selectedPiece.getPieceSymbol() == 'd') {
            selectedPiece.setVitality(dragon_initial_vitality);
        } else if (selectedPiece.getPieceSymbol() == 'S' || selectedPiece.getPieceSymbol() == 's') {
            selectedPiece.setVitality(squire_initial_vitality);
        } else if (selectedPiece.getPieceSymbol() == 'K' || selectedPiece.getPieceSymbol() == 'k') {
            selectedPiece.setVitality(knight_initial_vitality);
        } else if (selectedPiece.getPieceSymbol() == 'A' || selectedPiece.getPieceSymbol() == 'a') {
            selectedPiece.setVitality(archer_initial_vitality);
        }
    }
}
