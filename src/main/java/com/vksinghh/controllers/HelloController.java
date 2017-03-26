package com.vksinghh.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: vksinghh
 * Date: 3/26/17 6:44 PM
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
