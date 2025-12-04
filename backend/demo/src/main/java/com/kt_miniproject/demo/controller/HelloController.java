package com.kt_miniproject.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class HelloController {
    @GetMapping("/api/hello")
    public String hello(){
        return "Hello from Spring Boot!";
    }
}
