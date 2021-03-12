package com.trix.wowgarrisontracker.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Profile("spring")
public class LandingPage {

    @RequestMapping(value = {"","/"," "})
    public String defaultLandingPage(){
        return "redirect:/account/login/page";
    }
}
