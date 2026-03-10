package com.project1.project1.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(
//        origins = "https://p532-jakecole.github.io",
//        allowedHeaders = {"Authorization", "Content-Type"},
//        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
//)
@RestController
public class HomeController implements ErrorController {
    @GetMapping("/")
    public String greetings()
    {
        return "Welcome to the Trading Service!";
    }

}
