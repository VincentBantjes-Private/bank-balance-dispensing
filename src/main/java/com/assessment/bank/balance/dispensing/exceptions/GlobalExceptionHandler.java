package com.assessment.bank.balance.dispensing.exceptions;

import com.assessment.bank.balance.dispensing.api.models.exception.DefaultExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String X_RESPONSE_CODE = "x-response-code";
    public static final String X_RESPONSE_MESSAGE = "x-response-message";

    @ExceptionHandler(value = {ClientNotFoundException.class})
    public ResponseEntity<DefaultExceptionResponse> toResponse(ClientNotFoundException exception) {
        return createResponse(exception, exception.getMessage(), exception.getBusinessExceptionCode(), exception.getResponseStatus());
    }

    @ExceptionHandler(value = {ClientAccountNotFoundException.class})
    public ResponseEntity<DefaultExceptionResponse> toResponse(ClientAccountNotFoundException exception) {
        return createResponse(exception, exception.getMessage(), exception.getBusinessExceptionCode(), exception.getResponseStatus());
    }

    @ExceptionHandler(value = {ClientInsufficientFundsException.class})
    public ResponseEntity<DefaultExceptionResponse> toResponse(ClientInsufficientFundsException exception) {
        return createResponse(exception, exception.getMessage(), exception.getBusinessExceptionCode(), exception.getResponseStatus());
    }

    @ExceptionHandler(value = {AtmNotFoundException.class})
    public ResponseEntity<DefaultExceptionResponse> toResponse(AtmNotFoundException exception) {
        return createResponse(exception, exception.getMessage(), exception.getBusinessExceptionCode(), exception.getResponseStatus());
    }

    @ExceptionHandler(value = {AtmInsufficientFundsException.class})
    public ResponseEntity<DefaultExceptionResponse> toResponse(AtmInsufficientFundsException exception) {
        return createResponse(exception, exception.getMessage(), exception.getBusinessExceptionCode(), exception.getResponseStatus());
    }

    @ExceptionHandler(value = {ClientLimitExceededException.class})
    public ResponseEntity<DefaultExceptionResponse> toResponse(ClientLimitExceededException exception) {
        return createResponse(exception, exception.getMessage(), exception.getBusinessExceptionCode(), exception.getResponseStatus());
    }

    private ResponseEntity<DefaultExceptionResponse> createResponse(Exception e, String message, String businessExceptionCode, HttpStatus httpStatus) {
        log.info("{}, returning response with http status code: {}", e.getClass().getSimpleName(), httpStatus);

        HttpHeaders headers = new HttpHeaders();

        headers.set(X_RESPONSE_CODE, businessExceptionCode);
        headers.set(X_RESPONSE_MESSAGE, message);

        return new ResponseEntity<>(
                createExceptionResponseEntity(message, businessExceptionCode),
                headers,
                httpStatus);
    }

    private DefaultExceptionResponse createExceptionResponseEntity(String exceptionMessage, String businessExceptionCode) {
        return DefaultExceptionResponse
                .builder()
                .exceptionMessage(exceptionMessage)
                .exceptionCode(businessExceptionCode)
                .build();
    }
}