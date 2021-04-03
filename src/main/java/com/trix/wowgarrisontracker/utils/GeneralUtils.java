package com.trix.wowgarrisontracker.utils;

import com.trix.wowgarrisontracker.model.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class GeneralUtils {

    public GeneralUtils() {
    }

    public long getCurrentlyLoggedUserId() {
        return this.getCustomUserPrincipal().getId();
    }

    public CustomUserDetails getCustomUserPrincipal() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails accountDetails = null;

        try {
            accountDetails = (CustomUserDetails) authentication.getPrincipal();
        } catch (Exception e) {
            System.out.println("Logged User parsing error:" + e.getMessage());
        }

        return accountDetails;

    }
}
