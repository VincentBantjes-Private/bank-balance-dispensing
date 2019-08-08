package com.assessment.bank.balance.dispensing.api.models.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultExceptionResponse implements Serializable {

    @JsonProperty("message")
    private String exceptionMessage;

    @JsonProperty("code")
    private String exceptionCode;
}
