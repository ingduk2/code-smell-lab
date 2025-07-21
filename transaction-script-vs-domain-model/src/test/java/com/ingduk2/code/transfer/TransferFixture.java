package com.ingduk2.code.transfer;

import com.ingduk2.code.transfer.transactionscript.AccountTs;

import java.math.BigDecimal;

public class TransferFixture {

    public static AccountTs createAccountTs(BigDecimal balance) {
        return new AccountTs(balance);
    }
}
