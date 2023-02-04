package com.microservices.simpleService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HelloController {

	@GetMapping("hello")
	public String hello() {
		return "Hello world!";
	}
}
