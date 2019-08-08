package com.assessment.bank.balance.dispensing.jpa;

import java.math.BigDecimal;

public interface AggregateFinancialPositionReportDataDTO {

    String getClient();

    BigDecimal getLoanBalance();

    BigDecimal getTransactionalBalance();

    BigDecimal getTotal();

    Integer getNetPosition();
}