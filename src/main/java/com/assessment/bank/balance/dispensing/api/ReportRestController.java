package com.assessment.bank.balance.dispensing.api;

import com.assessment.bank.balance.dispensing.api.models.response.reportrestcontroller.AggregateFinancialPositionReportResponse;
import com.assessment.bank.balance.dispensing.api.models.response.reportrestcontroller.TransactionalBalanceReportResponse;
import com.assessment.bank.balance.dispensing.services.ReportManagementService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SwaggerDefinition
@RequestMapping("/report")
public class ReportRestController {

    private ReportManagementService reportManagementService;

    public ReportRestController(ReportManagementService reportManagementService) {
        this.reportManagementService = reportManagementService;
    }

    @GetMapping(value = "/transactional/balances")
    @ApiOperation(value = "View a list of clients with their highest transactional account balance.", response = TransactionalBalanceReportResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A list of clients with their highest transactional account balance are returned.", response = TransactionalBalanceReportResponse.class)
    })
    @ResponseStatus(HttpStatus.OK)
    public TransactionalBalanceReportResponse transactionalBalancesReport() {
        return reportManagementService.generateTransactionalBalancesReport();
    }

    @GetMapping(value = "/transactional/aggregatefinancialposition")
    @ApiOperation(value = "View a list of clients with their aggregate financial positions calculated.", response = AggregateFinancialPositionReportResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A list of clients with their aggregate financial positions are returned.", response = AggregateFinancialPositionReportResponse.class)
    })
    @ResponseStatus(HttpStatus.OK)
    public AggregateFinancialPositionReportResponse aggregateFinancialPositionReport() {
        return reportManagementService.generateAggregateFinancialPositionReport();
    }
}