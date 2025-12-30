package com.hy.haeyoback.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {
    
    @GetMapping("/")
    public String index() {
        return "redirect:/login.html";
    }
    
    @GetMapping("/map")
    public String map() {
        return "index.html";
    }
}