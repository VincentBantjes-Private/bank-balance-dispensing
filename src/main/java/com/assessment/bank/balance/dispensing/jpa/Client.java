package com.assessment.bank.balance.dispensing.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CLIENT")
@Access(AccessType.FIELD)
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "CLIENT_ID", length = 10)
    private Integer clientId;

    @Column(name = "TITLE", length = 10)
    private String title;

    @NotNull
    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME", length = 100)
    private String surname;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "DOB", length = 100)
    private Date dob;

    @ManyToOne(targetEntity = ClientSubType.class)
    @JoinColumn(name = "CLIENT_SUB_TYPE_CODE")
    private ClientSubType clientSubType;

    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ClientAccount> clientAccounts = new ArrayList<>();
}