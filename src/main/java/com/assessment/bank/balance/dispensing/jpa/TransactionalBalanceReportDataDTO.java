package com.assessment.bank.balance.dispensing.jpa;

import java.math.BigDecimal;

public interface TransactionalBalanceReportDataDTO {

    Integer getClientId();

    String getClientSurname();

    String getClientAccountNumber();

    String getAccountDescription();

    BigDecimal getDisplayBalance();
}