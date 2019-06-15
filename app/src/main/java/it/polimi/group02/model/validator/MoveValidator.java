package it.polimi.group02.model.validator;


/**
 * Implementing an interface allows a class to become more formal about the behavior it promises to provide. Interfaces form a
 * contract between the class and the outside world, and this contract is enforced at build time by the compiler. If your class
 * claims to implement an interface, all methods defined by that interface must appear in its source code before the class will
 * successfully compile.
 *
 * This interface defines the behaviour a move validator should abide by.
 */
public interface MoveValidator {

    /**
     * This method is implemented by the two classes AnyMoveValidator and HorizontalVerticalMoveValidator in 2 different ways:
     * the first one checks for a path in all the nearby cells, while the second one only on the horizontal and vertical directions
     * @return true if this kind of move is allowed
     */
    boolean checkMove();
}
