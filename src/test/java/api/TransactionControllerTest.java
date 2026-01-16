package api;

import api.dto.TransactionRequest;
import domain.AuthorizationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.CardService;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TransactionControllerTest {

    @Mock
    private CardService service;

    @InjectMocks
    private TransactionController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void transact_success() {
        TransactionRequest request = new TransactionRequest("123456789", "1234", BigDecimal.valueOf(100));

        when(service.authorizeTransaction(request.cardNumber(), request.cardPassword(), request.amount()))
                .thenReturn(AuthorizationResult.OK);

        ResponseEntity<String> response = controller.authorize(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("OK");
    }

    @Test
    void transact_cardNotFound() {
        TransactionRequest request = new TransactionRequest("123456789", "1234", BigDecimal.valueOf(100));

        when(service.authorizeTransaction(request.cardNumber(), request.cardPassword(), request.amount()))
                .thenReturn(AuthorizationResult.CARD_NOT_FOUND);

        ResponseEntity<String> response = controller.authorize(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isEqualTo("CARTAO_INEXISTENTE");
    }

    @Test
    void transact_invalidPassword() {
        TransactionRequest request = new TransactionRequest("123456789", "1234", BigDecimal.valueOf(100));

        when(service.authorizeTransaction(request.cardNumber(), request.cardPassword(), request.amount()))
                .thenReturn(AuthorizationResult.INVALID_PASSWORD);

        ResponseEntity<String> response = controller.authorize(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isEqualTo("SENHA_INVALIDA");
    }

    @Test
    void transact_insufficientBalance() {
        TransactionRequest request = new TransactionRequest("123456789", "1234", BigDecimal.valueOf(1000));

        when(service.authorizeTransaction(request.cardNumber(), request.cardPassword(), request.amount()))
                .thenReturn(AuthorizationResult.INSUFFICIENT_BALANCE);

        ResponseEntity<String> response = controller.authorize(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isEqualTo("SALDO_INSUFICIENTE");
     }
}