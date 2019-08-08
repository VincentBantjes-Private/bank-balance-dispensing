package com.assessment.bank.balance.dispensing.api.models.request.clientrestcontroller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawRequest implements Serializable {

    @NotNull
    private Integer atmId;
    @NotNull
    private String accountNumber;
    @NotNull
    private Integer requiredAmount;
}
