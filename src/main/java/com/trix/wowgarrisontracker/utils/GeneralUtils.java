package com.trix.wowgarrisontracker.utils;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Component;


@Component
public class GeneralUtils {
    

    public Cookie extractCookie(String cookieName, Cookie [] cookies){

        for(Cookie tmp : cookies){
            if(cookieName.equals(tmp.getName()))
                return tmp;
        }

        return null;

    }

}
