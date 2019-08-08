package com.assessment.bank.balance.dispensing.services;

import com.assessment.bank.balance.dispensing.api.models.request.clientrestcontroller.WithdrawRequest;
import com.assessment.bank.balance.dispensing.api.models.response.clientrestcontroller.Account;
import com.assessment.bank.balance.dispensing.api.models.response.clientrestcontroller.AccountConvertedCurrency;
import com.assessment.bank.balance.dispensing.api.models.response.clientrestcontroller.ClientBalanceResponse;
import com.assessment.bank.balance.dispensing.api.models.response.clientrestcontroller.WithdrawResponse;
import com.assessment.bank.balance.dispensing.exceptions.*;
import com.assessment.bank.balance.dispensing.jpa.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Slf4j
@Service
public class ClientManagementService {

    private ClientRepository clientRepository;
    private AtmRepository atmRepository;
    private AtmAllocationRepository atmAllocationRepository;

    public ClientManagementService(ClientRepository clientRepository, AtmRepository atmRepository, AtmAllocationRepository atmAllocationRepository) {
        this.clientRepository = clientRepository;
        this.atmRepository = atmRepository;
        this.atmAllocationRepository = atmAllocationRepository;
    }

    public ClientBalanceResponse getBalances(@NotNull
                                                     Integer clientId,
                                             @NotNull
                                                     LocalDateTime timestamp,
                                             Boolean transactional) throws ClientNotFoundException, ClientAccountNotFoundException {

        log.info("Client {} requested the balances for their accounts. Transactional account filtered = {}. Timestamp received {}",
                clientId, transactional != null ? transactional : false, timestamp);

        ClientBalanceResponse clientBalanceResponse = new ClientBalanceResponse();
        for (ClientAccount clientAccount : getClient(clientId).getClientAccounts()) {
            if (transactional != null && transactional != clientAccount.getAccountType().isTransactional()) {
                continue;
            }
            // The accounts are already sorted because of the compareTo of the Account object.
            clientBalanceResponse.getAccounts().add(
                    Account.builder()
                            .accountType(clientAccount.getAccountType().getDescription())
                            .accountBalance(clientAccount.getDisplayBalance())
                            .accountNumber(clientAccount.getClientAccountNumber())
                            .build());
        }

        return clientBalanceResponse;
    }

    public ClientBalanceResponse getCurrencyBalances(@NotNull
                                                             Integer clientId,
                                                     @NotNull
                                                             LocalDateTime timestamp,
                                                     Boolean transactional) throws ClientNotFoundException, ClientAccountNotFoundException {

        log.info("Client {} requested the converted balances for their accounts. Transactional account filtered = {}. Timestamp received {}",
                clientId, transactional != null ? transactional : false, timestamp);

        ClientBalanceResponse clientBalanceResponse = new ClientBalanceResponse();
        for (ClientAccount clientAccount : getClient(clientId).getClientAccounts()) {
            if (transactional != null && transactional != clientAccount.getAccountType().isTransactional()) {
                continue;
            }
            // The accounts are already sorted because of the compareTo of the Account object.
            AccountConvertedCurrency accountConvertedCurrency = new AccountConvertedCurrency(convertToRSA(clientAccount), clientAccount.getCurrency().getCurrencyCode(), clientAccount.getCurrency().getCurrencyConversionRate().getRate());
            accountConvertedCurrency.setAccountType(clientAccount.getAccountType().getDescription());
            accountConvertedCurrency.setAccountBalance(clientAccount.getDisplayBalance());
            accountConvertedCurrency.setAccountNumber(clientAccount.getClientAccountNumber());
            clientBalanceResponse.getAccounts().add(accountConvertedCurrency);
        }

        return clientBalanceResponse;
    }

    public WithdrawResponse withdraw(Integer clientId, LocalDateTime timestamp, WithdrawRequest withdrawRequest) throws AtmNotFoundException, AtmInsufficientFundsException, ClientNotFoundException, ClientAccountNotFoundException, ClientInsufficientFundsException, ClientLimitExceededException {

        log.info("Client {} requested to withdraw {}  from their {} account. Timestamp received {}",
                clientId, withdrawRequest.getRequiredAmount(), withdrawRequest.getAccountNumber(), timestamp);

        // This does not need to be synchronized because only one client can be busy at one time for the given ATM.
        Optional<Atm> atm = atmRepository.findById(withdrawRequest.getAtmId());

        if (!atm.isPresent() || atm.get().getAtmAllocations().size() <= 0) {
            throw new AtmNotFoundException("ATM not registered or unfunded", HttpStatus.NOT_FOUND);
        }

        boolean atmHasMoreThanRequested = validateMaxAtmAllocationVsRequestedAmount(atm.get(), withdrawRequest.getRequiredAmount());

        if (!atmHasMoreThanRequested) {
            throw new AtmInsufficientFundsException("Amount not available, would you like to draw " + maxForAtm(atm.get()), HttpStatus.BAD_REQUEST);
        }

        WithdrawResponse withdrawResponse = calculateNearestAmount(clientId, withdrawRequest.getAccountNumber(), atm.get(), BigDecimal.valueOf(withdrawRequest.getRequiredAmount()));

        for (Integer note : withdrawResponse.getNotesToReturn().keySet()) {
            for (AtmAllocation atmAllocation : atm.get().getAtmAllocations()) {
                if (atmAllocation.getDenomination().getValue().intValue() == note) {
                    atmAllocation.setCount(atmAllocation.getCount() - withdrawResponse.getNotesToReturn().get(note));
                    atmAllocationRepository.save(atmAllocation);
                    break;
                }
            }
        }

        return withdrawResponse;
    }

    // This is so that the calculation for the client's available funds can only be done at one ATM
    private synchronized WithdrawResponse calculateNearestAmount(Integer clientId, String accountNumber, Atm atm, BigDecimal amountToWithdraw) throws ClientNotFoundException, ClientAccountNotFoundException, ClientInsufficientFundsException, AtmInsufficientFundsException, ClientLimitExceededException {
        checkClientAvailableFunds(clientId, accountNumber, amountToWithdraw);

        Map<Denomination, Integer> countPerDenomination = new TreeMap<>();

        // Populate count per denomination
        for (AtmAllocation atmAllocation : atm.getAtmAllocations()) {
            if (atmAllocation.getDenomination().getDenominationType().getDenominationTypeCode().equals("N") && atmAllocation.getCount() > 0) {
                countPerDenomination.put(atmAllocation.getDenomination(), atmAllocation.getCount());
            }
        }

        WithdrawResponse withdrawResponse = new WithdrawResponse();

        BigDecimal total = BigDecimal.ZERO;
        int remainingAmount = amountToWithdraw.intValueExact();

        for (Denomination denomination : countPerDenomination.keySet()) {
            int quotient = remainingAmount / denomination.getValue().intValueExact();
            if (quotient > 0) {
                withdrawResponse.getNotesToReturn().put(denomination.getValue().intValue(), Integer.min(Integer.valueOf(quotient), countPerDenomination.get(denomination)));
                total = total.add(denomination.getValue().multiply(BigDecimal.valueOf(quotient)));
                remainingAmount -= denomination.getValue().intValue() * Integer.min(quotient, countPerDenomination.get(denomination));
            }
        }

        if (remainingAmount > 0) {
            throw new AtmInsufficientFundsException("Amount not available, would you like to draw " + total, HttpStatus.BAD_REQUEST);
        }

        updateClientBalance(clientId, accountNumber, amountToWithdraw);

        return withdrawResponse;
    }

    private void updateClientBalance(Integer clientId, String accountNumber, BigDecimal amountToWithdraw) throws ClientAccountNotFoundException, ClientNotFoundException {
        Client client = getClient(clientId);
        for (ClientAccount clientAccount : client.getClientAccounts()) {

            if (!clientAccount.getClientAccountNumber().equals(accountNumber)) {
                continue;
            }

            if (clientAccount.getCurrency().getCurrencyCode().equals("ZAR")) {
                clientAccount.setDisplayBalance(clientAccount.getDisplayBalance().subtract(amountToWithdraw));
            } else {
                clientAccount.setDisplayBalance(clientAccount.getDisplayBalance().subtract(convertToOtherCurrency(clientAccount.getCurrency(), amountToWithdraw)));
            }
            break;
        }

        clientRepository.saveAndFlush(client);
    }

    private BigDecimal convertToOtherCurrency(Currency currency, BigDecimal amountToWithdraw) {
        switch (currency.getCurrencyConversionRate().getConversionIndicator()) {
            case "/": {
                return amountToWithdraw.multiply(currency.getCurrencyConversionRate().getRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            case "*": {
                return amountToWithdraw.divide(currency.getCurrencyConversionRate().getRate(), 2, RoundingMode.HALF_UP);
            }
            default:
                return amountToWithdraw;
        }
    }

    private void checkClientAvailableFunds(Integer clientId, String accountNumber, BigDecimal amountToWithdraw) throws ClientAccountNotFoundException, ClientNotFoundException, ClientInsufficientFundsException, ClientLimitExceededException {
        boolean found = false;
        for (ClientAccount clientAccount : getClient(clientId).getClientAccounts()) {

            if (!clientAccount.getClientAccountNumber().equals(accountNumber)) {
                continue;
            }

            found = true;

            if (convertToRSA(clientAccount).compareTo(amountToWithdraw) < 0) {
                throw new ClientInsufficientFundsException("Insufficient funds", HttpStatus.BAD_REQUEST);
            }

            if(clientAccount.getAccountType().getAccountTypeCode().equals("CCRD") && clientAccount.getCreditCardLimit() != null && clientAccount.getCreditCardLimit().getAccountLimit().compareTo(amountToWithdraw) < 0) {
                throw new ClientLimitExceededException("Limit exceeded for a single transaction.", HttpStatus.BAD_REQUEST);
            }

            break;
        }

        if (!found) {
            throw new ClientAccountNotFoundException("Account not found", HttpStatus.NOT_FOUND);
        }
    }

    private boolean validateMaxAtmAllocationVsRequestedAmount(Atm atm, Integer amountToWithdraw) {

        if (maxForAtm(atm).compareTo(BigDecimal.valueOf(amountToWithdraw)) >= 0) {
            return true;
        }
        return false;
    }

    private BigDecimal maxForAtm(Atm atm) {
        BigDecimal total = BigDecimal.ZERO;
        for (AtmAllocation atmAllocation : atm.getAtmAllocations()) {
            if (atmAllocation.getDenomination().getDenominationType().getDenominationTypeCode().equals("N")) {
                total = total.add(atmAllocation.getDenomination().getValue().multiply(BigDecimal.valueOf(atmAllocation.getCount())));
            }
        }
        return total;
    }

    private Client getClient(Integer clientId) throws ClientNotFoundException, ClientAccountNotFoundException {
        Optional<Client> clientDBResponse = clientRepository.findById(clientId);

        if (!clientDBResponse.isPresent()) {
            throw new ClientNotFoundException("Client could not be found", HttpStatus.NOT_FOUND);
        }

        if (clientDBResponse.get().getClientAccounts().size() <= 0) {
            throw new ClientAccountNotFoundException("No accounts to display", HttpStatus.NOT_FOUND);
        }

        return clientDBResponse.get();
    }

    private BigDecimal convertToRSA(ClientAccount clientAccount) {
        switch (clientAccount.getCurrency().getCurrencyConversionRate().getConversionIndicator()) {
            case "/": {
                return clientAccount.getDisplayBalance().divide(clientAccount.getCurrency().getCurrencyConversionRate().getRate(), 2, RoundingMode.HALF_UP);
            }
            case "*": {
                return clientAccount.getDisplayBalance().multiply(clientAccount.getCurrency().getCurrencyConversionRate().getRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            default:
                return clientAccount.getDisplayBalance();
        }
    }
}