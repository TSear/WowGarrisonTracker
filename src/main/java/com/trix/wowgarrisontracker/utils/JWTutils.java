package com.trix.wowgarrisontracker.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Service
public class JWTutils {

    private final String KEY = "key123456";
    private Logger logger = LoggerFactory.getLogger(Slf4j.class);

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String username) {
        
        String token = Jwts.builder().setClaims(claims)
        .setSubject(username)
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .signWith(SignatureAlgorithm.HS256, KEY).compact();
        
        logger.info("Token JWTS : " + token);

        return token;
    }

}
