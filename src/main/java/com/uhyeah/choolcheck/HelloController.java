package com.uhyeah.choolcheck;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HelloController {

    @GetMapping("/hello")
    public String HelloAPI() {
        System.out.println("Test Success");
        return "Test Success";
    }

}
