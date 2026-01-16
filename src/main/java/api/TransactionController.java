package api;

import api.dto.TransactionRequest;
import domain.AuthorizationResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CardService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final CardService service;

    public TransactionController(CardService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> transact(@RequestBody TransactionRequest request) {

        AuthorizationResult result = service.authorizeTransaction(
                request.cardNumber(),
                request.cardPassword(),
                request.amount()
        );

        return switch (result) {
            case OK -> ResponseEntity.status(HttpStatus.CREATED).body("OK");
            case CARD_NOT_FOUND -> ResponseEntity.unprocessableEntity().body("CARD_NOT_FOUND");
            case INVALID_PASSWORD -> ResponseEntity.unprocessableEntity().body("INVALID_PASSWORD");
            case INSUFFICIENT_BALANCE -> ResponseEntity.unprocessableEntity().body("INSUFFICIENT_BALANCE");
        };
    }
}

