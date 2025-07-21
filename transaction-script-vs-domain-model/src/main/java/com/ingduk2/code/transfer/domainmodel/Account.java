package com.ingduk2.code.transfer.domainmodel;

import com.ingduk2.code.shared.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends AbstractEntity {

    private BigDecimal balance;

    public Account(BigDecimal balance) {
        this.balance = balance;
    }

    public void withdraw(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("잔액이 부족합니다");
        }

        this.balance = this.balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("입금액이 0 보다 작거나 같습니다");
        }

        this.balance = this.balance.add(amount);
    }

    public void transferTo(Account target, BigDecimal amount) {
        if (this.getId().equals(target.getId())) {
            throw new IllegalArgumentException("같은 계좌 간 이체는 불가능합니다");
        }

        this.withdraw(amount);
        target.deposit(amount);
    }
}
