package com.assessment.bank.balance.dispensing.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DenominationRepository extends JpaRepository<Denomination, Integer> {}