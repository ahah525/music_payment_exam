package com.ll.exam.app__2022_10_11.app.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adm")
public class AdminHomeController {
    @GetMapping("")
    public String showIndex() {
        return "redirect:/adm/home/main";
    }

    // 관리자 메인페이지
    @GetMapping("/home/main")
    public String showMain() {
        return "adm/home/main";
    }
}
