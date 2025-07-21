package com.ingduk2.code.transfer.domainmodel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow();

        account.withdraw(amount);

        return accountRepository.save(account);
    }

    @Transactional
    public Account deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow();

        account.deposit(amount);

        return accountRepository.save(account);
    }

    @Transactional
    public void transfer(TransferRequest request) {
        request.validateSelfTransferNotAllowed();

        Account from = accountRepository.findById(request.fromAccountId()).orElseThrow();
        Account to = accountRepository.findById(request.toAccountId()).orElseThrow();

        from.transferTo(to, request.amount());

        accountRepository.save(from);
        accountRepository.save(to);
    }
}
