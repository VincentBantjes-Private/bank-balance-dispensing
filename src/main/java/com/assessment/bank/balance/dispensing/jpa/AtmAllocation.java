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
@Table(name = "ATM_ALLOCATION")
@Access(AccessType.FIELD)
public class AtmAllocation implements Serializable {

    @Id
    @SequenceGenerator(name="seq_PK",sequenceName="SEQ_ATM_ALLOCATION", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_PK")
    @NotNull
    @Column(name = "ATM_ALLOCATION_ID", length = 10)
    private Integer atmAllocationId;

    @NotNull
    @ManyToOne(targetEntity = Atm.class)
    @JoinColumn(name = "ATM_ID")
    private Atm atm;

    @NotNull
    @ManyToOne(targetEntity = Denomination.class)
    @JoinColumn(name = "DENOMINATION_ID")
    private Denomination denomination;

    @NotNull
    @Column(name = "COUNT", length = 10)
    private Integer count;

    @Override
    public String toString() {
        return "AtmAllocation{" +
                "atmAllocationId=" + atmAllocationId +
                ", denomination=" + denomination +
                ", count=" + count +
                '}';
    }
}