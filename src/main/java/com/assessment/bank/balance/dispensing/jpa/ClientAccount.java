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
@Table(name = "CLIENT_ACCOUNT")
@Access(AccessType.FIELD)
public class ClientAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "CLIENT_ACCOUNT_NUMBER", length = 10)
    private String clientAccountNumber;

    @NotNull
    @ManyToOne(targetEntity = Client.class)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    @NotNull
    @ManyToOne(targetEntity = AccountType.class)
    @JoinColumn(name = "ACCOUNT_TYPE_CODE")
    private AccountType accountType;

    @NotNull
    @ManyToOne(targetEntity = Currency.class)
    @JoinColumn(name = "CURRENCY_CODE")
    private Currency currency;

    @Column(name = "DISPLAY_BALANCE", precision = 18, scale = 3)
    private BigDecimal displayBalance;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "CLIENT_ACCOUNT_NUMBER", referencedColumnName = "CLIENT_ACCOUNT_NUMBER")
    private CreditCardLimit creditCardLimit;

    @Override
    public String toString() {
        return "ClientAccount{" +
                "clientAccountNumber='" + clientAccountNumber + '\'' +
                ", accountType=" + accountType +
                ", currency=" + currency +
                ", displayBalance=" + displayBalance +
                ", creditCardLimit=" + creditCardLimit +
                '}';
    }
}