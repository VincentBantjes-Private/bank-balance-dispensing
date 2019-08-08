package com.assessment.bank.balance.dispensing.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.assessment.bank.balance.dispensing.jpa")
public class PersistenceConfig {
}