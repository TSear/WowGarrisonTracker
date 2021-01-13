package com.trix.wowgarrisontracker.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trix.wowgarrisontracker.model.CustomUserDetails;
import com.trix.wowgarrisontracker.services.implementation.AccountDetailsService;
import com.trix.wowgarrisontracker.utils.JWTutils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private AccountDetailsService accountDetailsService;

    private JWTutils jwTutils;

    public JWTFilter(JWTutils jwTutils, AccountDetailsService accountDetailsService) {
        this.jwTutils = jwTutils;
        this.accountDetailsService = accountDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        String login = null;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            login = jwTutils.getUsername(token);
        }

        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = CustomUserDetails.class
                    .cast(accountDetailsService.loadUserByUsername(login));
            if (jwTutils.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
