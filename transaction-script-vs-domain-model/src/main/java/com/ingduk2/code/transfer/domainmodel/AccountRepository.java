package com.ingduk2.code.transfer.domainmodel;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface AccountRepository extends Repository<Account, Long> {
    Optional<Account> findById(Long accountId);

    Account save(Account account);
}
