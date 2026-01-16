package api;

import api.dto.TransactionRequest;
import domain.AuthorizationResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CardService;

@RestController
@RequestMapping("/transacoes")
public class TransactionController {

    private final CardService service;

    public TransactionController(CardService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> authorize(@RequestBody TransactionRequest request) {

        AuthorizationResult result = service.authorizeTransaction(
                request.cardNumber(),
                request.cardPassword(),
                request.amount()
        );

        if (result.isApproved()) {
            return ResponseEntity.status(HttpStatus.CREATED).body("OK");
        }

        return ResponseEntity
                .unprocessableEntity()
                .body(result.getContractValue());
    }
}

