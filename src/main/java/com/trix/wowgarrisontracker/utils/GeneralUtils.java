package com.trix.wowgarrisontracker.utils;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class GeneralUtils {

    public GeneralUtils() {
    }

    public long getCurrentlyLoggedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = 0L;
        try {
            CustomUserDetails accountDetails = (CustomUserDetails) authentication.getPrincipal();
            id = accountDetails.getId();
        }catch (Exception e){
            System.out.println("Logged User parsing error: " + e.getMessage());
        }

        return id;
    }
}
