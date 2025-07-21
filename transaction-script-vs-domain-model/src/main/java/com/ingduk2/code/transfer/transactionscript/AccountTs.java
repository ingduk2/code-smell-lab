package com.ingduk2.code.transfer.transactionscript;

import com.ingduk2.code.shared.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountTs extends AbstractEntity {

    private BigDecimal balance;

    public AccountTs(BigDecimal balance) {
        this.balance = balance;
    }
}
