package com.assessment.bank.balance.dispensing.api;

import com.assessment.bank.balance.dispensing.api.models.exception.DefaultExceptionResponse;
import com.assessment.bank.balance.dispensing.api.models.request.clientrestcontroller.WithdrawRequest;
import com.assessment.bank.balance.dispensing.api.models.response.clientrestcontroller.ClientBalanceResponse;
import com.assessment.bank.balance.dispensing.api.models.response.clientrestcontroller.WithdrawResponse;
import com.assessment.bank.balance.dispensing.exceptions.*;
import com.assessment.bank.balance.dispensing.services.ClientManagementService;
import io.swagger.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RestController
@SwaggerDefinition
@RequestMapping("/client")
public class ClientRestController {

    private ClientManagementService clientManagementService;

    public ClientRestController(ClientManagementService clientManagementService) {
        this.clientManagementService = clientManagementService;
    }

    @GetMapping(value = "/{clientId}/balances")
    @ApiOperation(value = "View all accounts with the available balances. Filter by transactional accounts available.", response = ClientBalanceResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A list of account with balances are returned.", response = ClientBalanceResponse.class),
            @ApiResponse(code = 404, message = "An exception is returned when the client could not be found for the given client id, or there are no accounts for the client.", response = DefaultExceptionResponse.class)
    })
    @ResponseStatus(HttpStatus.OK)
    public ClientBalanceResponse viewAccountBalance(@ApiParam(required = true, value = "The id for the client for whom the accounts need to be retrieved.")
                                                    @NotNull
                                                    @PathVariable("clientId")
                                                            Integer clientId,
                                                    @ApiParam(required = true, value = "The timestamp of the request.")
                                                    @NotNull
                                                    @RequestParam(value = "timestamp")
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                            LocalDateTime timestamp,
                                                    @ApiParam(value = "Specifies of only transactional accounts need to be returned.")
                                                    @RequestParam(value = "transactional", required = false)
                                                            Boolean transactional) throws ClientNotFoundException, ClientAccountNotFoundException {
        return clientManagementService.getBalances(clientId, timestamp, transactional);
    }

    @GetMapping(value = "/{clientId}/balances/currencies")
    @ApiOperation(value = "View all accounts with the available balances converted to rand values. Filter by transactional accounts available.", response = ClientBalanceResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A list of account with balances are returned.", response = ClientBalanceResponse.class),
            @ApiResponse(code = 404, message = "An exception is returned when the client could not be found for the given client id, or there are no accounts for the client.", response = DefaultExceptionResponse.class)
    })
    @ResponseStatus(HttpStatus.OK)
    public ClientBalanceResponse viewAccountCurrencyBalance(@ApiParam(required = true, value = "The id for the client for whom the accounts need to be retrieved.")
                                                            @NotNull
                                                            @PathVariable("clientId")
                                                                    Integer clientId,
                                                            @ApiParam(required = true, value = "The timestamp of the request.")
                                                            @NotNull
                                                            @RequestParam(value = "timestamp")
                                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                    LocalDateTime timestamp,
                                                            @ApiParam(value = "Specifies of only transactional accounts need to be returned.")
                                                            @RequestParam(value = "transactional", required = false)
                                                                    Boolean transactional) throws ClientNotFoundException, ClientAccountNotFoundException {
        return clientManagementService.getCurrencyBalances(clientId, timestamp, transactional);
    }

    @PostMapping(value = "/{clientId}/withdraw")
    @ApiOperation(value = "Withdraw the amount from the account specified for the specified client.", response = WithdrawResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "The outcome of the withdrawal.", response = WithdrawResponse.class),
            @ApiResponse(code = 400, message = "An exception is returned when the client has insufficient funds, or if the amount is not available in the ATM.", response = DefaultExceptionResponse.class),
            @ApiResponse(code = 404, message = "An exception is returned when the client or atm could not be found, or if there are no accounts for the client.", response = DefaultExceptionResponse.class)
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public WithdrawResponse withdraw(@ApiParam(required = true, value = "The id for the client who is doing the withdrawal.")
                                          @NotNull
                                          @PathVariable("clientId")
                                                  Integer clientId,
                                          @ApiParam(required = true, value = "The timestamp of the request.")
                                          @NotNull
                                          @RequestParam(value = "timestamp")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                  LocalDateTime timestamp,
                                          @ApiParam(value = "The details for the withdrawal.")
                                          @RequestBody
                                                      WithdrawRequest withdrawRequest) throws ClientNotFoundException, ClientAccountNotFoundException, ClientInsufficientFundsException, AtmNotFoundException, AtmInsufficientFundsException, ClientLimitExceededException {
        return clientManagementService.withdraw(clientId, timestamp, withdrawRequest);
    }
}