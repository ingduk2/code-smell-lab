package com.ingduk2.code.transfer.transactionscript;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static com.ingduk2.code.transfer.TransferFixture.createAccountTs;

@SpringBootTest
@Transactional
record AccountTsServiceSpringBootTest(
        AccountTsService sut,
        AccountTsRepository accountTsRepository,
        EntityManager entityManager
) {

    @Nested
    class WithDraw {
        @Test
        @DisplayName("잔액이 부족하면 출금 실패")
        void fail1() {
            AccountTs account = saveAccount(300);

            Long accountId = account.getId();
            BigDecimal amount = BigDecimal.valueOf(500);

            Assertions.assertThatThrownBy(() -> sut.withdraw(accountId, amount))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void success1() {
            AccountTs account = saveAccount(1000);

            Long accountId = account.getId();
            BigDecimal amount = BigDecimal.valueOf(500);

            AccountTs result = sut.withdraw(accountId, amount);

            assertThat(result.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(500));
        }
    }

    @Nested
    class Deposit {
        @Test
        @DisplayName("입금액이 0 보다 같거나 작은 경우 실패")
        void fail1() {
            AccountTs account = saveAccount(300);

            Long accountId = account.getId();
            BigDecimal amount = BigDecimal.valueOf(0);

            Assertions.assertThatThrownBy(() -> sut.deposit(accountId, amount))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void success1() {
            AccountTs account = saveAccount(1000);

            Long accountId = account.getId();
            BigDecimal amount = BigDecimal.valueOf(500);

            AccountTs result = sut.deposit(accountId, amount);

            assertThat(result.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1500));
        }
    }

    @Nested
    class TransferTo {
        @Test
        @DisplayName("같은 계좌 간 이체시 실패")
        void fail1() {
            AccountTs account = saveAccount(300);

            Long fromAccountId = account.getId();
            Long toAccountId = account.getId();
            BigDecimal amount = BigDecimal.valueOf(500);

            Assertions.assertThatThrownBy(() -> sut.transfer(fromAccountId, toAccountId, amount))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("출금 계좌의 잔액이 부족 할 시 실패")
        void fail2() {
            AccountTs account1 = saveAccount(300);
            AccountTs account2 = saveAccount(1000);

            Long fromAccountId = account1.getId();
            Long toAccountId = account2.getId();
            BigDecimal amount = BigDecimal.valueOf(500);

            Assertions.assertThatThrownBy(() -> sut.transfer(fromAccountId, toAccountId, amount))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void success1() {
            AccountTs account1 = saveAccount(600);
            AccountTs account2 = saveAccount(1000);

            Long fromAccountId = account1.getId();
            Long toAccountId = account2.getId();
            BigDecimal amount = BigDecimal.valueOf(500);

            sut.transfer(fromAccountId, toAccountId, amount);

            SoftAssertions.assertSoftly(softly -> {
                AccountTs fromAccount = accountTsRepository.findById(fromAccountId).get();
                AccountTs toAccount = accountTsRepository.findById(toAccountId).get();

                softly.assertThat(fromAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(100));
                softly.assertThat(toAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1500));
            });
        }
    }

    private AccountTs saveAccount(int amount) {
        return accountTsRepository.save(createAccountTs(BigDecimal.valueOf(amount)));
    }
}