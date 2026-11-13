package uz.devruz.bank.exception;

/**
 * Parent of every business error in the bank.
 * Catching BankException catches all of them at once.
 */
public class BankException extends RuntimeException {

    public BankException(String message) {
        super(message);
    }
}
