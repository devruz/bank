package uz.devruz.bank.exception;

import java.math.BigDecimal;

/** Thrown when a withdrawal or transfer asks for more money than the account holds. */
public class InsufficientFundsException extends BankException {

    private final BigDecimal balance;
    private final BigDecimal requested;

    public InsufficientFundsException(BigDecimal balance, BigDecimal requested) {
        super("Insufficient funds: balance " + balance + ", requested " + requested);
        this.balance = balance;
        this.requested = requested;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getRequested() {
        return requested;
    }
}
