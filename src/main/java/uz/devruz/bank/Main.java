package uz.devruz.bank;

import uz.devruz.bank.account.Account;
import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        Account account = new Account(1, "Behruz");

        account.deposit(new BigDecimal("500.00"));
        account.withdraw(new BigDecimal("120.50"));
        System.out.println("Balance: " + account.getBalance());

        try {
            account.withdraw(new BigDecimal("10000"));
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}