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
@Table(name = "ATM")
@Access(AccessType.FIELD)
public class Atm implements Serializable {

    @Id
    @NotNull
    @Column(name = "ATM_ID", length = 10)
    private Integer atmId;

    @NotNull
    @Column(name = "NAME", length = 10)
    private String name;

    @NotNull
    @Column(name = "LOCATION")
    private String location;

    @OneToMany(
            mappedBy = "atm",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AtmAllocation> atmAllocations = new ArrayList<>();
}