package com.ingduk2.code.transfer.transactionscript;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface AccountTsRepository extends Repository<AccountTs, Long> {
    Optional<AccountTs> findById(Long accountId);

    AccountTs save(AccountTs account);
}
