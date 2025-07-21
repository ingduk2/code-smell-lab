package com.ingduk2.code.transfer.transactionscript;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static com.ingduk2.code.transfer.TransferFixture.createAccountTs;

class AccountTsServiceMockTest {

    AccountTsService sut;
    AccountTsRepository accountTsRepository;

    @BeforeEach
    void setUp() {
        accountTsRepository = Mockito.mock(AccountTsRepository.class);
        sut = new AccountTsService(accountTsRepository);
    }

    @Nested
    class WithDraw {
        @Test
        @DisplayName("잔액이 부족하면 출금 실패")
        void fail1() {
            Long accountId = 1L;
            BigDecimal amount = BigDecimal.valueOf(500);

            given(accountTsRepository.findById(accountId))
                    .willReturn(Optional.of(createAccountTs(BigDecimal.valueOf(300))));

            Assertions.assertThatThrownBy(() -> sut.withdraw(accountId, amount))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void success1() {
            Long accountId = 1L;
            BigDecimal amount = BigDecimal.valueOf(500);

            AccountTs account = createAccountTs(BigDecimal.valueOf(1000));
            given(accountTsRepository.findById(accountId))
                    .willReturn(Optional.of(account));
            given(accountTsRepository.save(account))
                    .willReturn(account);

            AccountTs result = sut.withdraw(accountId, amount);

            assertThat(result.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(500));
        }
    }

    @Nested
    class Deposit {
        @Test
        @DisplayName("입금액이 0 보다 같거나 작은 경우 실패")
        void fail1() {
            Long accountId = 1L;
            BigDecimal amount = BigDecimal.valueOf(0);

            given(accountTsRepository.findById(accountId))
                    .willReturn(Optional.of(createAccountTs(BigDecimal.valueOf(300))));

            Assertions.assertThatThrownBy(() -> sut.deposit(accountId, amount))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void success1() {
            Long accountId = 1L;
            BigDecimal amount = BigDecimal.valueOf(500);

            AccountTs account = createAccountTs(BigDecimal.valueOf(1000));
            given(accountTsRepository.findById(accountId))
                    .willReturn(Optional.of(account));
            given(accountTsRepository.save(account))
                    .willReturn(account);

            AccountTs result = sut.deposit(accountId, amount);

            assertThat(result.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1500));
        }
    }

    @Nested
    class TransferTo {
        @Test
        @DisplayName("같은 계좌 간 이체시 실패")
        void fail1() {
            Long fromId = 1L;
            Long toId = 1L;
            BigDecimal amount = BigDecimal.valueOf(500);

            given(accountTsRepository.findById(fromId))
                    .willReturn(Optional.of(createAccountTs(BigDecimal.valueOf(1000))));

            Assertions.assertThatThrownBy(() -> sut.transfer(fromId, toId, amount))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("출금 계좌의 잔액이 부족 할 시 실패")
        void fail2() {
            Long fromId = 1L;
            Long toId = 2L;
            BigDecimal amount = BigDecimal.valueOf(500);

            given(accountTsRepository.findById(fromId))
                    .willReturn(Optional.of(createAccountTs(BigDecimal.valueOf(100))));
            given(accountTsRepository.findById(toId))
                    .willReturn(Optional.of(createAccountTs(BigDecimal.valueOf(500))));

            Assertions.assertThatThrownBy(() -> sut.transfer(fromId, toId, amount))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void success1() {
            Long fromId = 1L;
            Long toId = 2L;
            BigDecimal amount = BigDecimal.valueOf(500);

            AccountTs fromAccount = createAccountTs(BigDecimal.valueOf(500));
            given(accountTsRepository.findById(fromId))
                    .willReturn(Optional.of(fromAccount));
            AccountTs toAccount = createAccountTs(BigDecimal.valueOf(1000));
            given(accountTsRepository.findById(toId))
                    .willReturn(Optional.of(toAccount));

            sut.transfer(fromId, toId, amount);

            assertThat(fromAccount.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(toAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1500));
        }
    }
}