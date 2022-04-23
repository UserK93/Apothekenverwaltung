package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class ApothekeApplication {

	@Autowired
	public static void main(String[] args) {
		SpringApplication.run(ApothekeApplication.class, args);
	}



}
