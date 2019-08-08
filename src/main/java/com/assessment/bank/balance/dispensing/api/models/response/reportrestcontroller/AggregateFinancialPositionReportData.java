package com.assessment.bank.balance.dispensing.api.models.response.reportrestcontroller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregateFinancialPositionReportData implements Serializable {

    private String client;
    private BigDecimal loanBalance;
    private BigDecimal transactionalBalance;
    private Integer netPosition;
}