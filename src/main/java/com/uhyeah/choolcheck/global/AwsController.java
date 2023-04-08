package com.uhyeah.choolcheck.global;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AwsController {

    @GetMapping("/aws")
    public ResponseEntity<Object> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
