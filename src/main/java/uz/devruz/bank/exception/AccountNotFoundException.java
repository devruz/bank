package uz.devruz.bank.exception;

/** Thrown when no account exists with the given id. */
public class AccountNotFoundException extends BankException {

    private final long accountId;

    public AccountNotFoundException(long accountId) {
        super("Account not found: " + accountId);
        this.accountId = accountId;
    }

    public long getAccountId() {
        return accountId;
    }
}
