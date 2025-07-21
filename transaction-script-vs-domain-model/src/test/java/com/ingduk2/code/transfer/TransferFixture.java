package com.ingduk2.code.transfer;

import com.ingduk2.code.transfer.domainmodel.Account;
import com.ingduk2.code.transfer.transactionscript.AccountTs;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

public class TransferFixture {

    public static Account createAccount(Long id, BigDecimal balance) {
        Account account = new Account(balance);
        ReflectionTestUtils.setField(account, "id", id);
        return account;
    }

    public static AccountTs createAccountTs(BigDecimal balance) {
        return new AccountTs(balance);
    }
}
