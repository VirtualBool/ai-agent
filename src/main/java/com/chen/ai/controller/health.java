package com.chen.ai.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/health")
@RestController
public class health {

    @RequestMapping("/check")
    public String check(){
        return "ok";
    }
}
