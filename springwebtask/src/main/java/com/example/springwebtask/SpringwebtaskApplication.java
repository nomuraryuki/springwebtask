package com.example.springwebtask;

import com.example.springwebtask.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringwebtaskApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringwebtaskApplication.class, args);
//		var context =SpringApplication.run(SpringwebtaskApplication.class, args);
//		var userService = context.getBean(UserService.class);
//		var list = userService.findUser("admin","admin");

	}

}
