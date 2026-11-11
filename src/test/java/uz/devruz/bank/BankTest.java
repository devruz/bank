package uz.devruz.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import uz.devruz.bank.account.Account;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
    }

    @Nested
    class OpenAccount {

        @Test
        void assignsSequentialIdsStartingAtOne() {
            Account first = bank.openAccount("Behruz");
            Account second = bank.openAccount("Jonibek");

            assertEquals(1, first.getId());
            assertEquals(2, second.getId());
            assertEquals("Behruz", first.getOwnerName());
            assertEquals(0, first.getBalance().compareTo(BigDecimal.ZERO));
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", "   "})
        void rejectsBlankOwnerName(String ownerName) {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> bank.openAccount(ownerName));
            assertEquals("Owner name must not be blank", e.getMessage());
            assertTrue(bank.getAccounts().isEmpty());
        }
    }

    @Nested
    class FindAccount {

        @Test
        void returnsOpenedAccount() {
            Account account = bank.openAccount("Behruz");
            assertSame(account, bank.findAccount(account.getId()).orElseThrow());
        }

        @Test
        void returnsEmptyForUnknownId() {
            assertTrue(bank.findAccount(42).isEmpty());
        }
    }

    @Nested
    class GetAccounts {

        @Test
        void returnsAccountsInOpeningOrder() {
            Account first = bank.openAccount("Behruz");
            Account second = bank.openAccount("Jonibek");
            assertEquals(List.of(first, second), bank.getAccounts());
        }

        @Test
        void returnedListCannotBeModified() {
            bank.openAccount("Behruz");
            assertThrows(UnsupportedOperationException.class,
                    () -> bank.getAccounts().clear());
            assertEquals(1, bank.getAccounts().size());
        }
    }

    @Nested
    class Transfer {

        private Account from;
        private Account to;

        @BeforeEach
        void setUp() {
            from = bank.openAccount("Behruz");
            to = bank.openAccount("Jonibek");
            from.deposit(new BigDecimal("500.00"));
        }

        @Test
        void movesMoneyBetweenAccountsById() {
            bank.transfer(from.getId(), to.getId(), new BigDecimal("100.00"));
            assertEquals(new BigDecimal("400.00"), from.getBalance());
            assertEquals(new BigDecimal("100.00"), to.getBalance());
        }

        @Test
        void rejectsUnknownSourceAccount() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> bank.transfer(99, to.getId(), new BigDecimal("100")));
            assertEquals("Account not found: 99", e.getMessage());
            assertEquals(0, to.getBalance().compareTo(BigDecimal.ZERO));
        }

        @Test
        void rejectsUnknownTargetAccountWithoutWithdrawing() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> bank.transfer(from.getId(), 99, new BigDecimal("100")));
            assertEquals("Account not found: 99", e.getMessage());
            assertEquals(new BigDecimal("500.00"), from.getBalance());
        }

        @Test
        void rejectsTransferToSameAccount() {
            assertThrows(IllegalArgumentException.class,
                    () -> bank.transfer(from.getId(), from.getId(), new BigDecimal("100")));
            assertEquals(new BigDecimal("500.00"), from.getBalance());
        }

        @Test
        void insufficientFundsLeavesBothBalancesUnchanged() {
            assertThrows(IllegalArgumentException.class,
                    () -> bank.transfer(from.getId(), to.getId(), new BigDecimal("99999")));
            assertEquals(new BigDecimal("500.00"), from.getBalance());
            assertEquals(0, to.getBalance().compareTo(BigDecimal.ZERO));
        }
    }
}
