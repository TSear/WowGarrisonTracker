package com.trix.wowgarrisontracker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {


    @RequestMapping(value = "errors", method = RequestMethod.GET)
    public String errorHandler(HttpServletRequest request, Model model){

        String message = "";
        int errorCode = getErrorCode(request);
        switch (errorCode) {
            case 400: {
                message = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                message = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 404: {
                message = "Http Error Code: 404. Resource not found";
                break;
            }
            case 500: {
                message = "Http Error Code: 500. Internal Server Error";
                break;
            }
        }
        model.addAttribute("errorMessage",message);
        return "errorPage";
    }

    private int getErrorCode(HttpServletRequest request) {
        return (Integer)request
                .getAttribute("javax.servlet.error.status_code");
    }
}
