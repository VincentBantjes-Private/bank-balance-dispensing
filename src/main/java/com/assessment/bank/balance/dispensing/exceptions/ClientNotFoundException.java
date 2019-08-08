package com.assessment.bank.balance.dispensing.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ClientNotFoundException extends Exception {

    private HttpStatus responseStatus;
    private String businessExceptionCode = "1";

    public ClientNotFoundException(HttpStatus httpStatus, String businessExceptionCode) {
        this.responseStatus = httpStatus;
        this.businessExceptionCode = businessExceptionCode;
    }

    public ClientNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.responseStatus = httpStatus;
    }

    public ClientNotFoundException(String message, HttpStatus responseStatus, String businessExceptionCode) {
        super(message);
        this.responseStatus = responseStatus;
        this.businessExceptionCode = businessExceptionCode;
    }

    public ClientNotFoundException(String message, Throwable throwable, HttpStatus httpStatus) {
        super(message, throwable);
        this.responseStatus = httpStatus;
    }

    public ClientNotFoundException(Throwable throwable, HttpStatus httpStatus) {
        super(throwable);
        this.responseStatus = httpStatus;
    }
}