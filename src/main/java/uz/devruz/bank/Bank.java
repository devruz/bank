package uz.devruz.bank;

import uz.devruz.bank.account.Account;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Bank {
    private final Map<Long, Account> accounts = new LinkedHashMap<>();
    private long nextId = 1;

    public Account openAccount(String ownerName) {
        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Owner name must not be blank");
        }
        Account account = new Account(nextId++, ownerName);
        accounts.put(account.getId(), account);
        return account;
    }

    public Optional<Account> findAccount(long id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public List<Account> getAccounts() {
        return List.copyOf(accounts.values());
    }

    public void transfer(long fromId, long toId, BigDecimal amount) {
        Account from = getAccountOrThrow(fromId);
        Account to = getAccountOrThrow(toId);
        from.transferTo(to, amount);
    }

    private Account getAccountOrThrow(long id) {
        return findAccount(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + id));
    }
}
