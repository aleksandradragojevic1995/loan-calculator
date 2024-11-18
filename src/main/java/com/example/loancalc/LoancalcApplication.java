package com.example.loancalc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LoancalcApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoancalcApplication.class, args);
	}

}
