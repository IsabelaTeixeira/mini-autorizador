package api;

import api.dto.CreateCardRequest;
import domain.Card;
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

class CardControllerTest {
    @Mock
    private CardService service;

    @InjectMocks
    private CardController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCard_success() {
        CreateCardRequest request = new CreateCardRequest("123456789", "1234");
        Card card = new Card("123456789", "1234");

        when(service.createCard(request.cardNumber(), request.password())).thenReturn(card);

        ResponseEntity<Card> response = controller.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(card);
    }

    @Test
    void createCard_alreadyExists() {
        CreateCardRequest request = new CreateCardRequest("123456789", "1234");

        when(service.createCard(request.cardNumber(), request.password()))
                .thenThrow(new IllegalArgumentException("Card exists"));

        ResponseEntity<Card> response = controller.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getCardNumber()).isEqualTo(request.cardNumber());
        assertThat(response.getBody().getPassword()).isEqualTo(request.password());
    }

    @Test
    void getBalance_success() {
        String cardNumber = "123456789";
        BigDecimal balance = BigDecimal.valueOf(500);

        when(service.getBalance(cardNumber)).thenReturn(balance);

        ResponseEntity<BigDecimal> response = controller.balance(cardNumber);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(balance);
    }

    @Test
    void getBalance_cardNotFound() {
        String cardNumber = "123456789";

        when(service.getBalance(cardNumber)).thenThrow(new IllegalArgumentException("Card not found"));

        ResponseEntity<BigDecimal> response = controller.balance(cardNumber);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }
}