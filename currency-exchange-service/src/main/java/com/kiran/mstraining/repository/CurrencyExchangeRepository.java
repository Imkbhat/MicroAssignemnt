package com.kiran.mstraining.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kiran.mstraining.model.CurrencyExchange;

public interface CurrencyExchangeRepository 
	extends JpaRepository<CurrencyExchange, Long> {

	CurrencyExchange findByFromAndTo(String from, String to);
}
