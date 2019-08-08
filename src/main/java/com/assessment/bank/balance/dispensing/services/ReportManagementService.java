package com.assessment.bank.balance.dispensing.services;

import com.assessment.bank.balance.dispensing.api.models.response.reportrestcontroller.AggregateFinancialPositionReportData;
import com.assessment.bank.balance.dispensing.api.models.response.reportrestcontroller.AggregateFinancialPositionReportResponse;
import com.assessment.bank.balance.dispensing.api.models.response.reportrestcontroller.TransactionalBalanceReportResponse;
import com.assessment.bank.balance.dispensing.jpa.AggregateFinancialPositionReportDataDTO;
import com.assessment.bank.balance.dispensing.jpa.ClientRepository;
import com.assessment.bank.balance.dispensing.api.models.response.reportrestcontroller.TransactionalBalanceReportData;
import com.assessment.bank.balance.dispensing.jpa.TransactionalBalanceReportDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReportManagementService {

    private ClientRepository clientRepository;
    private ClientManagementService clientManagementService;

    public ReportManagementService(ClientRepository clientRepository, ClientManagementService clientManagementService) {
        this.clientRepository = clientRepository;
        this.clientManagementService = clientManagementService;
    }

    public TransactionalBalanceReportResponse generateTransactionalBalancesReport() {
        List<TransactionalBalanceReportDataDTO> transactionalBalanceReportDataDTOS = clientRepository.fetchTransactionalBalanceReportDataDTO();

        TransactionalBalanceReportResponse transactionalBalanceReportResponse = new TransactionalBalanceReportResponse();
        for (TransactionalBalanceReportDataDTO transactionalBalanceReportData: transactionalBalanceReportDataDTOS) {
            transactionalBalanceReportResponse.getReportData().add(mapTransactionalBalanceReportData(transactionalBalanceReportData));
        }

        return transactionalBalanceReportResponse;
    }

    public AggregateFinancialPositionReportResponse generateAggregateFinancialPositionReport() {
        List<AggregateFinancialPositionReportDataDTO> aggregateFinancialPositionReportDataDTOS = clientRepository.fetchAggregateFinancialPositionReportDataDTO();

        AggregateFinancialPositionReportResponse aggregateFinancialPositionReportResponse = new AggregateFinancialPositionReportResponse();
        for (AggregateFinancialPositionReportDataDTO aggregateFinancialPositionReportData: aggregateFinancialPositionReportDataDTOS) {
            aggregateFinancialPositionReportResponse.getReportData().add(mapAggregateFinancialPositionReportData(aggregateFinancialPositionReportData));
        }

        return aggregateFinancialPositionReportResponse;
    }

    private TransactionalBalanceReportData mapTransactionalBalanceReportData(TransactionalBalanceReportDataDTO transactionalBalanceReportDataDTO) {
        TransactionalBalanceReportData transactionalBalanceReportData = new TransactionalBalanceReportData();
        transactionalBalanceReportData.setAccountDescription(transactionalBalanceReportDataDTO.getAccountDescription());
        transactionalBalanceReportData.setClientAccountNumber(transactionalBalanceReportDataDTO.getClientAccountNumber());
        transactionalBalanceReportData.setClientId(transactionalBalanceReportDataDTO.getClientId());
        transactionalBalanceReportData.setClientSurname(transactionalBalanceReportDataDTO.getClientSurname());
        transactionalBalanceReportData.setDisplayBalance(transactionalBalanceReportDataDTO.getDisplayBalance());
        return transactionalBalanceReportData;
    }

    private AggregateFinancialPositionReportData mapAggregateFinancialPositionReportData(AggregateFinancialPositionReportDataDTO aggregateFinancialPositionReportDataDTO) {
        AggregateFinancialPositionReportData aggregateFinancialPositionReportData = new AggregateFinancialPositionReportData();
        aggregateFinancialPositionReportData.setClient(aggregateFinancialPositionReportDataDTO.getClient());
        aggregateFinancialPositionReportData.setLoanBalance(aggregateFinancialPositionReportDataDTO.getLoanBalance());
        aggregateFinancialPositionReportData.setTransactionalBalance(aggregateFinancialPositionReportDataDTO.getTransactionalBalance());
        aggregateFinancialPositionReportData.setNetPosition(aggregateFinancialPositionReportDataDTO.getNetPosition());
        return aggregateFinancialPositionReportData;
    }
}