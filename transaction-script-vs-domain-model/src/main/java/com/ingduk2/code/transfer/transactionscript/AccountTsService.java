package com.ingduk2.code.transfer.transactionscript;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountTsService {

    private final AccountTsRepository accountTsRepository;

    @Transactional
    public AccountTs withdraw(Long accountId, BigDecimal amount) {
        AccountTs account = accountTsRepository.findById(accountId).orElseThrow();

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("잔액이 부족합니다");
        }

        account.setBalance(account.getBalance().subtract(amount));

        return accountTsRepository.save(account);
    }

    @Transactional
    public AccountTs deposit(Long accountId, BigDecimal amount) {
        AccountTs account = accountTsRepository.findById(accountId).orElseThrow();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("입금액이 0 보다 작거나 같습니다");
        }

        account.setBalance(account.getBalance().add(amount));

        return accountTsRepository.save(account);
    }

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("같은 계좌 간 이체는 불가능합니다");
        }

        AccountTs fromAccount = accountTsRepository.findById(fromAccountId).orElseThrow();
        AccountTs toAccount = accountTsRepository.findById(toAccountId).orElseThrow();

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("잔액이 부족합니다");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountTsRepository.save(fromAccount);
        accountTsRepository.save(toAccount);
    }
}
