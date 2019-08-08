package com.assessment.bank.balance.dispensing.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query(value = "SELECT client.CLIENT_ID as clientId, " +
            "client.SURNAME as clientSurname, " +
            "clientAccount.CLIENT_ACCOUNT_NUMBER as clientAccountNumber, " +
            "accountType.DESCRIPTION as accountDescription, " +
            "clientAccount.DISPLAY_BALANCE as displayBalance " +
            "FROM CLIENT client " +
            "JOIN CLIENT_ACCOUNT clientAccount ON client.CLIENT_ID = clientAccount.CLIENT_ID " +
            "JOIN " +
            "(SELECT CLIENT_ID, " +
            "MAX(DISPLAY_BALANCE) AS DISPLAY_BALANCE " +
            "FROM CLIENT_ACCOUNT clientAccount " +
            "JOIN ACCOUNT_TYPE accountType ON accountType.ACCOUNT_TYPE_CODE = clientAccount.ACCOUNT_TYPE_CODE " +
            "WHERE accountType.TRANSACTIONAL = 1 " +
            "GROUP BY clientAccount.CLIENT_ID" +
            ") accountFilter " +
            "ON clientAccount.CLIENT_ID = accountFilter.CLIENT_ID AND clientAccount.DISPLAY_BALANCE = accountFilter.DISPLAY_BALANCE " +
            "JOIN ACCOUNT_TYPE accountType ON accountType.ACCOUNT_TYPE_CODE = clientAccount.ACCOUNT_TYPE_CODE " +
            "WHERE accountType.TRANSACTIONAL = 1",
    nativeQuery = true)
    List<TransactionalBalanceReportDataDTO> fetchTransactionalBalanceReportDataDTO();

    @Query(value = "SELECT TRIM(CONCAT(client.TITLE, ' ', client.NAME, ' ', client.SURNAME)) as client, " +
            "loanAmount.totalLoan AS loanBalance, " +
            "transactionalAmount.totalTransactional As transactionalBalance, " +
            "loanAmount.totalLoan + transactionalAmount.totalTransactional AS total, " +
            "ROW_NUMBER() OVER (ORDER BY loanAmount.totalLoan + transactionalAmount.totalTransactional DESC) AS netPosition " +
            "FROM CLIENT client " +
            "LEFT JOIN " +
            "(SELECT client.CLIENT_ID, " +
            "SUM(CASE accountType.ACCOUNT_TYPE_CODE = 'PLOAN' OR accountType.ACCOUNT_TYPE_CODE = 'HLOAN' AND accountType.TRANSACTIONAL = 0 WHEN TRUE THEN DISPLAY_BALANCE ELSE 0 END) AS totalLoan " +
            "FROM Client client " +
            "LEFT JOIN CLIENT_ACCOUNT clientAccount ON client.CLIENT_ID = clientAccount.CLIENT_ID " +
            "JOIN ACCOUNT_TYPE accountType ON accountType.ACCOUNT_TYPE_CODE = clientAccount.ACCOUNT_TYPE_CODE " +
            "GROUP BY clientAccount.CLIENT_ID) loanAmount ON client.CLIENT_ID = loanAmount.CLIENT_ID " +
            "LEFT JOIN " +
            "(SELECT client.CLIENT_ID, " +
            "SUM(CASE accountType.TRANSACTIONAL = 1  WHEN TRUE THEN DISPLAY_BALANCE ELSE 0 END) AS totalTransactional " +
            "FROM CLIENT client " +
            "LEFT JOIN CLIENT_ACCOUNT clientAccount ON client.CLIENT_ID = clientAccount.CLIENT_ID " +
            "JOIN ACCOUNT_TYPE accountType ON accountType.ACCOUNT_TYPE_CODE = clientAccount.ACCOUNT_TYPE_CODE " +
            "GROUP BY client.CLIENT_ID) transactionalAmount ON client.CLIENT_ID = transactionalAmount.CLIENT_ID " +
            "ORDER BY netPosition",
            nativeQuery = true)
    List<AggregateFinancialPositionReportDataDTO> fetchAggregateFinancialPositionReportDataDTO();
}