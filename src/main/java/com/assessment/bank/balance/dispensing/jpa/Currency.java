package com.assessment.bank.balance.dispensing.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CURRENCY")
@Access(AccessType.FIELD)
public class Currency implements Serializable {

    @Id
    @NotNull
    @Column(name = "CURRENCY_CODE", length = 3)
    private String currencyCode;

    @NotNull
    @Column(name = "DECIMAL_PLACES", length = 10)
    private Integer decimalPlaces;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "CURRENCY_CODE", referencedColumnName = "CURRENCY_CODE")
    private CurrencyConversionRate currencyConversionRate;
}