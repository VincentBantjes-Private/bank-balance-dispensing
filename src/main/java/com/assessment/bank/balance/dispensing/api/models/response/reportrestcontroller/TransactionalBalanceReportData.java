package com.assessment.bank.balance.dispensing.api.models.response.reportrestcontroller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionalBalanceReportData implements Serializable {

    private Integer clientId;
    private String clientSurname;
    private String clientAccountNumber;
    private String accountDescription;
    private BigDecimal displayBalance;
}