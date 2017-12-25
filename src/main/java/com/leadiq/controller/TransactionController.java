package com.leadiq.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
	@RequestMapping("/")
	String home(){
		return "Hello Spring Boot!";
	}
}
