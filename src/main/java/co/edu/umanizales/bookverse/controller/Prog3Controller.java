package co.edu.umanizales.bookverse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prog3")

public class Prog3Controller {
    @GetMapping
    public String getHello(){
        return "Hola campeones y campeonas";
    }
}
