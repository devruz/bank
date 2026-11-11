package uz.devruz.bank.account;
import java.math.BigDecimal;
public class Account {
    private final long id;
    private final String ownerName;
    private BigDecimal balance;
    public Account(long id, String ownerName){
        this.id = id;
        this.ownerName = ownerName;
        this.balance = BigDecimal.ZERO;
    }
    public long getId(){
        return id;
    }
    public String getOwnerName(){
        return ownerName;
    }
    public BigDecimal getBalance(){
        return balance;
    }
    public void deposit(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance = balance.add(amount);
    }
    public void withdraw(BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive");
        } else if (amount.compareTo(balance) > 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance = balance.subtract(amount);
    }
}
