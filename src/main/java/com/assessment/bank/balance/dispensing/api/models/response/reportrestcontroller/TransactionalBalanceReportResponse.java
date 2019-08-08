package com.assessment.bank.balance.dispensing.api.models.response.reportrestcontroller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionalBalanceReportResponse implements Serializable {

    private List<TransactionalBalanceReportData> reportData = new ArrayList<>();
}