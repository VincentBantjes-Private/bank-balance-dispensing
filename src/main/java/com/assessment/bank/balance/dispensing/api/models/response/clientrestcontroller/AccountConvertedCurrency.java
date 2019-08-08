package com.assessment.bank.balance.dispensing.api.models.response.clientrestcontroller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountConvertedCurrency extends Account {

    private BigDecimal ZARAmount;
    private String currency;
    private BigDecimal conversionRate;
}