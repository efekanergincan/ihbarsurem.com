package com.example.efedemoihbar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.example.ihbaersuresi.Controller" })
public class EfedemoihbarApplication {

	public static void main(String[] args) {
		SpringApplication.run(EfedemoihbarApplication.class, args);
	}

}
