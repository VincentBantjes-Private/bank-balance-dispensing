package com.assessment.bank.balance.dispensing.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class AtmNotFoundException extends Exception {

    private HttpStatus responseStatus;
    private String businessExceptionCode = "4";

    public AtmNotFoundException(HttpStatus httpStatus, String businessExceptionCode) {
        this.responseStatus = httpStatus;
        this.businessExceptionCode = businessExceptionCode;
    }

    public AtmNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.responseStatus = httpStatus;
    }

    public AtmNotFoundException(String message, HttpStatus responseStatus, String businessExceptionCode) {
        super(message);
        this.responseStatus = responseStatus;
        this.businessExceptionCode = businessExceptionCode;
    }

    public AtmNotFoundException(String message, Throwable throwable, HttpStatus httpStatus) {
        super(message, throwable);
        this.responseStatus = httpStatus;
    }

    public AtmNotFoundException(Throwable throwable, HttpStatus httpStatus) {
        super(throwable);
        this.responseStatus = httpStatus;
    }
}