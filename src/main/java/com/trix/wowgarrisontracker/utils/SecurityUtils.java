package com.trix.wowgarrisontracker.utils;

import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

@Profile("vaadin")
@Component
public class SecurityUtils {


    public SecurityUtils() {
    }

    static public boolean isFrameworkInternalRequest(HttpServletRequest request) {

        String parameter = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);

        return parameter != null && Stream.of(HandlerHelper.RequestType.values())
                .anyMatch(requestType -> requestType.getIdentifier().equals(parameter));

    }


    static public boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }
}
