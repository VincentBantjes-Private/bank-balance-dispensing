package com.assessment.bank.balance.dispensing.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CLIENT_TYPE")
@Access(AccessType.FIELD)
public class ClientType implements Serializable {

    @Id
    @NotNull
    @Column(name = "CLIENT_TYPE_CODE", length = 2)
    private String clientTypeCode;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(
            mappedBy = "clientType",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ClientSubType> clientSubTypes = new ArrayList<>();

    @Override
    public String toString() {
        return "ClientType{" +
                "clientTypeCode='" + clientTypeCode + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}