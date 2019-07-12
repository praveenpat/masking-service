package com.masking.micoservices.maskingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan({"com.masking.micoservices.maskingservice","com.masking.micoservices.services"})
public class MaskingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaskingServiceApplication.class, args);
	}
	
	
	

}
