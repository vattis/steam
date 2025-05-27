package com.example.steam.module.home.presentation.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String gotoHome(){
        return "/main";
    }
}
