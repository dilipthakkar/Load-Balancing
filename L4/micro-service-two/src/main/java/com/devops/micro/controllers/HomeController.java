package com.devops.micro.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller class for handling request related to home endpoint.
 */
@RestController
public class HomeController {

    /**
     * The port of the server where the application is running.
     */
    @Value("${server.port}")
    private String port;

    /**
     * Handles GET request to the /home endpoint.
     * 
     * @return ResponseEntity containing the response body with the message
     *         indicating the server port from which the request is processed.
     */
    @GetMapping("/home")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("From Port: " + port);
    }

}

