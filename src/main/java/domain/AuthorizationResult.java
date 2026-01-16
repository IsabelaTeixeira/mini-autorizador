package domain;

public enum AuthorizationResult {

    OK("OK"),
    INSUFFICIENT_BALANCE("SALDO_INSUFICIENTE"),
    INVALID_PASSWORD("SENHA_INVALIDA"),
    CARD_NOT_FOUND("CARTAO_INEXISTENTE");

    private final String contractValue;

    AuthorizationResult(String contractValue) {
        this.contractValue = contractValue;
    }

    public String getContractValue() {
        return contractValue;
    }

    public boolean isApproved() {
        return this == OK;
    }
}
