package com.ingduk2.code.transfer.transactionscript;

import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AccountTsRepositoryStub implements AccountTsRepository {

    private final Map<Long, AccountTs> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public Optional<AccountTs> findById(Long accountId) {
        return Optional.ofNullable(store.get(accountId));
    }

    @Override
    public AccountTs save(AccountTs account) {
        if (account.getId() == null) {
            Long id = idGenerator.getAndIncrement();
            ReflectionTestUtils.setField(account, "id", id);
        }

        store.put(account.getId(), account);
        return account;
    }
}
