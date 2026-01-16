package service;

import domain.AuthorizationResult;
import domain.Card;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CardRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository repository;
    public CardService(CardRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AuthorizationResult  authorizeTransaction(String cardNumber, String password, BigDecimal amount) {

        Optional<Card> optionalCard = repository.findById(cardNumber);

        return optionalCard
                .map(card -> validate(card, password, amount))
                .orElse(AuthorizationResult.CARD_NOT_FOUND);
    }

    private AuthorizationResult validate(Card card, String password, BigDecimal amount) {

        if (!card.getPassword().equals(password)) {
            return AuthorizationResult.INVALID_PASSWORD;
        }

        if (card.getBalance().compareTo(amount) < 0) {
            return AuthorizationResult.INSUFFICIENT_BALANCE;
        }

        card.debit(amount);
        repository.save(card);

        return AuthorizationResult.OK;
    }

    public Card createCard(String cardNumber, String password) {
        if (repository.existsById(cardNumber)) {
            throw new IllegalArgumentException("Cartão já existe");
        }
        return repository.save(new Card(cardNumber, password));
    }
    public BigDecimal getBalance(String cardNumber) {
        return repository.findById(cardNumber)
                .map(Card::getBalance)
                .orElseThrow(() -> new IllegalArgumentException("Cartão não encontrado"));
    }
}
