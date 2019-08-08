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
@Table(name = "CURRENCY_CONVERSION_RATE")
@Access(AccessType.FIELD)
public class CurrencyConversionRate implements Serializable {

    @Id
    @NotNull
    @Column(name = "CURRENCY_CODE", length = 3)
    private String currencyCode;

    @NotNull
    @OneToOne(mappedBy = "currencyConversionRate", cascade = CascadeType.ALL)
    private Currency currency;

    @NotNull
    @Column(name = "CONVERSION_INDICATOR", length = 1)
    private String conversionIndicator;

    @NotNull
    @Column(name = "RATE", precision = 18, scale = 3)
    private BigDecimal rate;

    @Override
    public String toString() {
        return "CurrencyConversionRate{" +
                "currencyCode='" + currencyCode + '\'' +
                ", conversionIndicator='" + conversionIndicator + '\'' +
                ", rate=" + rate +
                '}';
    }
}