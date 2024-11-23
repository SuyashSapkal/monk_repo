package com.monk.java_implementation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class Index {
    @GetMapping()
    public ResponseEntity<String> getIndex(){
        return ResponseEntity.ok("Hello, this is the main index page");
    }
}
