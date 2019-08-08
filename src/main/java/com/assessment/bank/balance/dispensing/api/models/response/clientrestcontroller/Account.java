package com.assessment.bank.balance.dispensing.api.models.response.clientrestcontroller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account implements Serializable, Comparable<Account> {

    protected String accountNumber;
    protected String accountType;
    protected BigDecimal accountBalance;

    @Override
    public int compareTo(Account account) {
        return account.getAccountBalance().compareTo(accountBalance);
    }
}