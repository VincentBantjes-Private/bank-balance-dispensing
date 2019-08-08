package com.assessment.bank.balance.dispensing.api.models.response.clientrestcontroller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawResponse implements Serializable {

    private Map<Integer, Integer> notesToReturn = new TreeMap<>();
}