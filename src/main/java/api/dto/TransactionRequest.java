package api.dto;

import java.math.BigDecimal;

public record TransactionRequest(
        String cardNumber,
        String cardPassword,
        BigDecimal amount
) {}