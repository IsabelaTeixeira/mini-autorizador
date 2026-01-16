package domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @Column(length = 16, nullable = false, unique = true)
    @JsonProperty("numeroCartao")
    private String cardNumber;

    @Column(nullable = false)
    @JsonProperty("senha")
    private String password;

    @Column(nullable = false)
    private BigDecimal balance;

    protected Card() {}

    public Card(String cardNumber, String password) {
        this.cardNumber = cardNumber;
        this.password = password;
        this.balance = new BigDecimal("500.00");
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void debit(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }
}

