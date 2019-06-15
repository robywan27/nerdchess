package it.polimi.group02.model.validator;


/**
 * Implementing an interface allows a class to become more formal about the behavior it promises to provide. Interfaces form a
 * contract between the class and the outside world, and this contract is enforced at build time by the compiler. If your class
 * claims to implement an interface, all methods defined by that interface must appear in its source code before the class will
 * successfully compile.
 *
 * This interface defines the behaviour an attack validator should abide by.
 */
public interface AttackValidator {
    /**
     * This method ought to be implemented by any concrete implementation of an attack validator.
     * @return the outcome of the validation process: true means the attack shall be carried out; false determines the invalidity of the attack.
     */
    boolean checkAttack();
}
