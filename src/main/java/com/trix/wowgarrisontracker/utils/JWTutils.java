package com.trix.wowgarrisontracker.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@PropertySource("classpath:application.properties")
@Service
public class JWTutils {

    @Value("${config.jwt.key}")
    private String KEY;
    private Logger logger = LoggerFactory.getLogger(Slf4j.class);

    @Autowired
    private AccountService accountService;

    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }

    public String getUsername(String token) {
        return this.extractClaims(token).getSubject();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String username) {

        Account account = accountService.findUserByUsername(username);
        claims.put("accountId", account.getId());

        String token = Jwts.builder().setClaims(claims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, KEY).compact();

        logger.info("Token JWTS : " + token);

        return "Bearer_" + token;
    }

    public Boolean isExpired(String token) {
        return (this.extractClaims(token).getExpiration().after(new Date(System.currentTimeMillis())));
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        String login = this.getUsername(token);
        return (login.equals(userDetails.getUsername()) && this.isExpired(token));
    }

}
