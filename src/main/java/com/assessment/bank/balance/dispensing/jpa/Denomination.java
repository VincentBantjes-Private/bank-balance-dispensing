package com.assessment.bank.balance.dispensing.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DENOMINATION")
@Access(AccessType.FIELD)
public class Denomination implements Serializable, Comparable<Denomination> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "DENOMINATION_ID", precision = 10, unique = true)
    private Integer denominationId;

    @NotNull
    @Column(name = "VALUE", precision = 65535, scale = 32767)
    private BigDecimal value;

    @ManyToOne(targetEntity = DenominationType.class)
    @JoinColumn(name = "DENOMINATION_TYPE_CODE")
    private DenominationType denominationType;

    @Override
    public int compareTo(Denomination denomination) {
        return denomination.getValue().compareTo(value);
    }
}