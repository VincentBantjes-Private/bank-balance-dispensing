package com.assessment.bank.balance.dispensing.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DENOMINATION_TYPE")
@Access(AccessType.FIELD)
public class DenominationType implements Serializable {

    @Id
    @NotNull
    @Column(name = "DENOMINATION_TYPE_CODE", length = 1)
    private String denominationTypeCode;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;
}