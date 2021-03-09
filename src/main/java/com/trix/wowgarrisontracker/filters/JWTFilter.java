package com.trix.wowgarrisontracker.filters;

import com.trix.wowgarrisontracker.model.CustomUserDetails;
import com.trix.wowgarrisontracker.services.implementation.AccountDetailsService;
import com.trix.wowgarrisontracker.utils.JWTutils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private AccountDetailsService accountDetailsService;
    @Autowired
    private JWTutils jwTutils;


    //TODO To trzeba całe zmienić -> https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
    // TODO Trzeba obsłużyć SignatureException.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

//		logger.info(request.getRequestURI() + "");
        String token = request.getHeader("Authorization");
        String login = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> optionalJwtCookie = Arrays.asList(cookies)
                    .stream()
                    .filter(cookie -> cookie.getName()
                            .equals("Authorization"))
                    .findFirst();

            if (optionalJwtCookie.isPresent())
                token = optionalJwtCookie.get().getValue();

            try {

                if (token != null && token.startsWith("Bearer_")) {
                    token = token.substring(7);
                    login = jwTutils.getUsername(token);
                }

                if (login != null && SecurityContextHolder.getContext()
                        .getAuthentication() == null) {
                    CustomUserDetails userDetails = CustomUserDetails.class
                            .cast(accountDetailsService.loadUserByUsername(login));
                    if (jwTutils.isTokenValid(token, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext()
                                .setAuthentication(usernamePasswordAuthenticationToken);
                    }
                    // TODO Trzeba zrobić odnawianie jwt
                }

            } catch (ExpiredJwtException expiredJwtException) {
                System.out.println(expiredJwtException.getStackTrace());
            } catch (SignatureException e) {
                System.out.println(e.getStackTrace());
            }
        }
        filterChain.doFilter(request, response);
    }
}
