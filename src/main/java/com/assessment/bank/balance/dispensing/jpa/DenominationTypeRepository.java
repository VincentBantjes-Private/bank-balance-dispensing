package com.assessment.bank.balance.dispensing.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DenominationTypeRepository extends JpaRepository<DenominationType, String> {}