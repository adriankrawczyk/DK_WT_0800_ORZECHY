package com.example.nfz;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {


    @GetMapping
    public String greeting() {
        return "<h1> Technologie obiektowe - projekt </h1>";
    }


    @PostConstruct
    public void onControllerCreated(){
        System.out.println("greeting controller starting stuff...");
        System.out.println("go to http://localhost:8080/swagger-ui/index.html to see the functionality");
    }
}
