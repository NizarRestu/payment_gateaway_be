package com.payment.gateaway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class GateawayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GateawayApplication.class, args);
	}

}
