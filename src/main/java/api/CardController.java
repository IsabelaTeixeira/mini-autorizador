package api;

import api.dto.CreateCardRequest;
import domain.Card;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CardService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cartoes")
public class CardController {

    private final CardService service;

    public CardController(CardService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Card> create(@RequestBody CreateCardRequest request) {
        try {
            Card card = service.createCard(request.cardNumber(), request.password());
            return ResponseEntity.status(HttpStatus.CREATED).body(card);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                    new Card(request.cardNumber(), request.password())
            );
        }
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> balance(@PathVariable String cardNumber) {
        try {
            return ResponseEntity.ok(service.getBalance(cardNumber));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

