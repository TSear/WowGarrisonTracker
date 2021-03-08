package com.trix.wowgarrisontracker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LandingPage {

    @RequestMapping(value = {"","/"," "})
    public String defaultLandingPage(){
        return "redirect:/account/login/page";
    }
}
