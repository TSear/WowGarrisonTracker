package com.trix.wowgarrisontracker.utils;

import com.trix.wowgarrisontracker.converters.OptionsToOptionsDTO;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.CustomUserDetails;
import com.trix.wowgarrisontracker.model.Options;
import com.trix.wowgarrisontracker.model.Server;
import com.trix.wowgarrisontracker.pojos.OptionsDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class GeneralUtils {

    private OptionsToOptionsDTO optionsToOptionsDTO;

    public GeneralUtils() {
        this.optionsToOptionsDTO = new OptionsToOptionsDTO();
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


    public OptionsDTO getUserSettings() {
        Options accountOptions = this.getCustomUserPrincipal().getOptions();

        return optionsToOptionsDTO.convert(accountOptions);
    }
}
