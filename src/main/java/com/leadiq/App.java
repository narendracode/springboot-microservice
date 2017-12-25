package com.leadiq;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableAutoConfiguration
@SpringBootApplication
@RestController
public class App{
	
	@RequestMapping("/")
	String home(){
		return "Hello Spring Boot!";
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}
}
