package com.example.springratelimiterdemo.controller;

import com.example.springratelimiterdemo.payload.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class RateLimitController {

    @GetMapping(value = "/developers")
    public ResponseEntity<?> developers() {
        return ResponseEntity.status(200).body(new ApiResponse("developers endpoint success response"));
    }

    @GetMapping(value = "/organizations")
    public ResponseEntity<?> organizations() {

        return ResponseEntity.status(200).body(new ApiResponse("organizations endpoint success response"));
    }
}
