package com.assessment.bank.balance.dispensing.jpa;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "CLIENT_SUB_TYPE")
@Access(AccessType.FIELD)
public class ClientSubType implements Serializable {

    @Id
    @NotNull
    @Column(name = "CLIENT_SUB_TYPE_CODE", length = 4)
    private String clientSubTypeCode;

    @NotNull
    @ManyToOne(targetEntity = ClientType.class)
    @JoinColumn(name = "CLIENT_TYPE_CODE")
    private ClientType clientType;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;
}