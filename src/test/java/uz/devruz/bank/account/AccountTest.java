package uz.devruz.bank.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uz.devruz.bank.exception.InsufficientFundsException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(1, "Behruz");
    }

    @Test
    void newAccountHasIdOwnerAndZeroBalance() {
        assertEquals(1, account.getId());
        assertEquals("Behruz", account.getOwnerName());
        assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
    }

    @Nested
    class Deposit {

        @Test
        void increasesBalance() {
            account.deposit(new BigDecimal("500.00"));
            account.deposit(new BigDecimal("0.01"));
            assertEquals(new BigDecimal("500.01"), account.getBalance());
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "0.00", "-1", "-0.01"})
        void rejectsNonPositiveAmount(String amount) {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> account.deposit(new BigDecimal(amount)));
            assertEquals("Deposit amount must be positive", e.getMessage());
            assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
        }

        @Test
        void rejectsNullAmount() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> account.deposit(null));
            assertEquals("Deposit amount must not be null", e.getMessage());
            assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
        }
    }

    @Nested
    class Withdraw {

        @BeforeEach
        void fund() {
            account.deposit(new BigDecimal("500.00"));
        }

        @Test
        void decreasesBalance() {
            account.withdraw(new BigDecimal("120.50"));
            assertEquals(new BigDecimal("379.50"), account.getBalance());
        }

        @Test
        void allowsWithdrawingEntireBalance() {
            account.withdraw(new BigDecimal("500.00"));
            assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "-1", "-0.01"})
        void rejectsNonPositiveAmount(String amount) {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> account.withdraw(new BigDecimal(amount)));
            assertEquals("Withdraw amount must be positive", e.getMessage());
            assertEquals(new BigDecimal("500.00"), account.getBalance());
        }

        @Test
        void rejectsNullAmount() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> account.withdraw(null));
            assertEquals("Withdraw amount must not be null", e.getMessage());
            assertEquals(new BigDecimal("500.00"), account.getBalance());
        }

        @Test
        void rejectsAmountGreaterThanBalance() {
            InsufficientFundsException e = assertThrows(InsufficientFundsException.class,
                    () -> account.withdraw(new BigDecimal("500.01")));
            assertEquals("Insufficient funds: balance 500.00, requested 500.01", e.getMessage());
            assertEquals(new BigDecimal("500.00"), e.getBalance());
            assertEquals(new BigDecimal("500.01"), e.getRequested());
            assertEquals(new BigDecimal("500.00"), account.getBalance());
        }
    }

    @Nested
    class TransferTo {

        private Account friend;

        @BeforeEach
        void setUp() {
            account.deposit(new BigDecimal("500.00"));
            friend = new Account(2, "Jonibek");
        }

        @Test
        void movesMoneyBetweenAccounts() {
            account.transferTo(friend, new BigDecimal("100"));
            assertEquals(new BigDecimal("400.00"), account.getBalance());
            assertEquals(0, friend.getBalance().compareTo(new BigDecimal("100")));
        }

        @Test
        void rejectsNullTarget() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> account.transferTo(null, new BigDecimal("100")));
            assertEquals("Target account must not be null", e.getMessage());
            assertEquals(new BigDecimal("500.00"), account.getBalance());
        }

        @Test
        void rejectsTransferToSelf() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> account.transferTo(account, new BigDecimal("100")));
            assertEquals("Cannot transfer to the same account", e.getMessage());
            assertEquals(new BigDecimal("500.00"), account.getBalance());
        }

        @Test
        void insufficientFundsLeavesBothBalancesUnchanged() {
            assertThrows(InsufficientFundsException.class,
                    () -> account.transferTo(friend, new BigDecimal("99999")));
            assertEquals(new BigDecimal("500.00"), account.getBalance());
            assertEquals(0, friend.getBalance().compareTo(BigDecimal.ZERO));
        }

        @Test
        void nullAmountLeavesBothBalancesUnchanged() {
            assertThrows(IllegalArgumentException.class,
                    () -> account.transferTo(friend, null));
            assertEquals(new BigDecimal("500.00"), account.getBalance());
            assertEquals(0, friend.getBalance().compareTo(BigDecimal.ZERO));
        }

        @Test
        void nonPositiveAmountLeavesBothBalancesUnchanged() {
            assertThrows(IllegalArgumentException.class,
                    () -> account.transferTo(friend, BigDecimal.ZERO));
            assertEquals(new BigDecimal("500.00"), account.getBalance());
            assertEquals(0, friend.getBalance().compareTo(BigDecimal.ZERO));
        }
    }
}
