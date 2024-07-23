package ru.raccoon.netologydiploma.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RequestController {

    @GetMapping("hello")
    public String getHello() {
        return "Hello World!";
    }
}
