package com.kiran.mstraining.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.kiran.mstraining.model.CurrencyConversion;
import com.kiran.mstraining.service.CurrencyExchangeProxy;

@RestController
public class CurrencyConversionController {
	
	@Autowired
	private CurrencyExchangeProxy exchangeProxy;
	
	@Autowired
	private Environment env;
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion getCurrencyConversion(@PathVariable("from") String from, 
						@PathVariable("to") String to, 
						@PathVariable("quantity") BigDecimal quantity) {
		
		HashMap<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		
		ResponseEntity<CurrencyConversion> forEntity = 
				new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", 
				CurrencyConversion.class, uriVariables);
		
		CurrencyConversion currConv = forEntity.getBody();
		String property = env.getProperty("local.server.port");
		
		CurrencyConversion currencyConversion = new CurrencyConversion(1000L, currConv.getFrom(), currConv.getTo(), 
				currConv.getConversionMultiple(), quantity, quantity.multiply(currConv.getConversionMultiple()));
		
		currencyConversion.setEnvironment(property);
		
		return currencyConversion;
		
	}
	
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion getCurrencyConversionFeign(@PathVariable("from") String from, 
						@PathVariable("to") String to, 
						@PathVariable("quantity") BigDecimal quantity) {
		
		
		
		CurrencyConversion currConv = exchangeProxy.getExchangeValue(from, to);
		
		String property = env.getProperty("local.server.port").concat("-feign");
		
		CurrencyConversion currencyConversion = new CurrencyConversion(1000L, currConv.getFrom(), currConv.getTo(), 
				currConv.getConversionMultiple(), quantity, quantity.multiply(currConv.getConversionMultiple()));
		
		currencyConversion.setEnvironment(currConv.getEnvironment());
		
		return currencyConversion;
		
	}
}
