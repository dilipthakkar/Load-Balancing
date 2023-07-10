package com.devops.micro.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/home")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("From Port: " + port);
    }

}
