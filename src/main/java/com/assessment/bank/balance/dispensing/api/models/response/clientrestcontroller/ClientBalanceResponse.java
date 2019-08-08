package com.assessment.bank.balance.dispensing.api.models.response.clientrestcontroller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientBalanceResponse implements Serializable {

    private Set<Account> accounts = new TreeSet<>();
}