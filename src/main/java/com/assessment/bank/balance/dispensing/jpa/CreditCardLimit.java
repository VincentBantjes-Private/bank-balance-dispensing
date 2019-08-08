package com.assessment.bank.balance.dispensing.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CREDIT_CARD_LIMIT")
@Access(AccessType.FIELD)
public class CreditCardLimit implements Serializable {

    @Id
    @NotNull
    @Column(name = "CLIENT_ACCOUNT_NUMBER", length = 10)
    private String clientAccountNumber;

    @NotNull
    @OneToOne(mappedBy = "creditCardLimit", cascade = CascadeType.ALL)
    private ClientAccount clientAccount;

    @NotNull
    @Column(name = "ACCOUNT_LIMIT", precision = 18, scale = 3)
    private BigDecimal accountLimit;

    @Override
    public String toString() {
        return "CreditCardLimit{" +
                "clientAccountNumber='" + clientAccountNumber + '\'' +
                ", accountLimit=" + accountLimit +
                '}';
    }
}