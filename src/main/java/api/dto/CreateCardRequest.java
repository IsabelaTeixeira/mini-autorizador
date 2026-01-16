package api.dto;

public record CreateCardRequest(
        String cardNumber,
        String password
) {}
