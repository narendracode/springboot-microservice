package com.leadiq;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;


@EnableAutoConfiguration
@SpringBootApplication
public class App{
	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}
}
