package com.hy.haeyoback.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {
    
    @GetMapping("/")
    public String index() {
        // 루트 경로는 바로 지도 페이지로 이동
        return "redirect:/index.html";
    }
    
    @GetMapping("/map")
    public String map() {
        return "redirect:/index.html";
    }
}