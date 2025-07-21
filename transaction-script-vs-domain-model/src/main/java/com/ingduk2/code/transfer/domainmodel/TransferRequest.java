package com.ingduk2.code.transfer.domainmodel;

import java.math.BigDecimal;

public record TransferRequest(
        Long fromAccountId,
        Long toAccountId,
        BigDecimal amount
) {
    public void validateSelfTransferNotAllowed() {
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("같은 계좌 간 이체는 불가능합니다");
        }
    }
}
