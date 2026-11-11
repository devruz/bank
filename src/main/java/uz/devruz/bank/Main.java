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

        Account friend = new Account(2, "Jonibek");
        account.transferTo(friend, new BigDecimal("100"));
        System.out.println("My balance: " + account.getBalance());
        System.out.println("Friend balance: " + friend.getBalance());
        try {
            account.transferTo(friend, new BigDecimal("99999"));
        } catch(IllegalArgumentException e) {
            System.out.println("Friend balance after failed transfer: " + friend.getBalance());
        }
    }
}