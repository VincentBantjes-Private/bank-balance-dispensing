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
@Table(name = "ACCOUNT_TYPE")
@Access(AccessType.FIELD)
public class AccountType implements Serializable {

    @Id
    @NotNull
    @Column(name = "ACCOUNT_TYPE_CODE", length = 10, unique = true)
    private String accountTypeCode;

    @NotNull
    @Column(name = "DESCRIPTION", length = 50)
    private String description;

    @Column(name = "TRANSACTIONAL")
    private boolean transactional;
}