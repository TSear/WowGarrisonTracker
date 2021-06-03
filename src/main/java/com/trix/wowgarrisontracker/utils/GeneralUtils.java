package com.trix.wowgarrisontracker.utils;

import com.trix.wowgarrisontracker.model.CustomUserDetails;
import com.trix.wowgarrisontracker.model.Options;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class GeneralUtils {


    public GeneralUtils() {

    }

    public static long getCurrentlyLoggedUserId() {
        return GeneralUtils.getCustomUserPrincipal().getId();
    }

    public static CustomUserDetails getCustomUserPrincipal() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails accountDetails = null;

        try {
            accountDetails = (CustomUserDetails) authentication.getPrincipal();
        } catch (Exception e) {
            System.out.println("Logged User parsing error:" + e.getMessage());
        }

        return accountDetails;

    }


    public static Options getUserSettings() {

        return getCustomUserPrincipal().getOptions();
    }
}
