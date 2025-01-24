package com.weinne.finance_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.weinne.finance_system") // Adicione esta linha
public class FinanceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceSystemApplication.class, args);
	}

}
