package com.ingduk2.code.transfer.domainmodel;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static com.ingduk2.code.transfer.TransferFixture.*;

class AccountTest {

    @Nested
    class WithDraw {
        @Test
        @DisplayName("잔액이 부족하면 출금 실패")
        void fail1() {
            Account account = createAccount(1L, BigDecimal.valueOf(100));

            Assertions.assertThatThrownBy(() -> account.withdraw(BigDecimal.valueOf(500)))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void success1() {
            Account account = createAccount(1L, BigDecimal.valueOf(1000));

            account.withdraw(BigDecimal.valueOf(700));

            assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(300));
        }
    }

    @Nested
    class Deposit {
        @Test
        @DisplayName("입금액이 0 보다 같거나 작은 경우 실패")
        void fail1() {
            Account account = createAccount(1L, BigDecimal.valueOf(100));

            assertThatThrownBy(() -> account.deposit(BigDecimal.valueOf(-100)))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void success1() {
            Account account = createAccount(1L, BigDecimal.valueOf(1000));

            account.deposit(BigDecimal.valueOf(400));

            assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1400));
        }
    }

    @Nested
    class TransferTo {
        @Test
        @DisplayName("같은 계좌 간 이체시 실패")
        void fail1() {
            Account fromAccount = createAccount(1L, BigDecimal.valueOf(500));
            BigDecimal amount = BigDecimal.valueOf(1000);

            assertThatThrownBy(() -> fromAccount.transferTo(fromAccount, amount))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("출금 계좌의 잔액이 부족 할 시 실패")
        void fail2() {
            Account fromAccount = createAccount(1L, BigDecimal.valueOf(500));
            Account toAccount = createAccount(2L, BigDecimal.valueOf(1000));
            BigDecimal amount = BigDecimal.valueOf(1000);

            assertThatThrownBy(() -> fromAccount.transferTo(toAccount, amount))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void success1() {
            Account fromAccount = createAccount(1L, BigDecimal.valueOf(500));
            Account toAccount = createAccount(2L, BigDecimal.valueOf(1000));
            BigDecimal amount = BigDecimal.valueOf(400);

            fromAccount.transferTo(toAccount, amount);

            assertThat(fromAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(100));
            assertThat(toAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1400));
        }
    }
}