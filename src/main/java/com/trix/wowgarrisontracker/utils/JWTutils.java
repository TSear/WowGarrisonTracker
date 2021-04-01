package com.trix.wowgarrisontracker.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;



@Service
public class JWTutils {

    private String KEY = "M.Ds9ZR}C4q1WDvcxnD57H.Og([Ep";


//    private Logger logger = LoggerFactory.getLogger(Slf4j.class);

    @Autowired
    private AccountService accountService;

    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }

    public String getUsername(String token) {
        return this.extractClaims(token).getSubject();
    }

    public String generateToken(UserDetails userDetails) {
    
        return createToken(userDetails.getUsername());
    }
//TODO To nie ma sensu. Do przerobienia;
    public String generateToken(String username) {
    	return createToken(username);
    }

    private String createToken(String username) {

        Map<String, Object> claims = new HashMap<>();
        Account account = accountService.findUserByUsername(username);
        claims.put("accountId", account.getId());

        String token = Jwts.builder().setClaims(claims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, KEY).compact();

//        logger.info("Token JWTS : " + token);

        return "Bearer_" + token;
    }

    public Boolean isExpired(String token) {
        return (this.extractClaims(token).getExpiration().before(new Date(System.currentTimeMillis())));
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        String login = this.getUsername(token);
        return (login.equals(userDetails.getUsername()) && this.isExpired(token));
    }

    public Long extractId(String token){
        return Long.valueOf((int)this.extractClaims(token.substring(7)).get("accountId"));
    }

    public Long extractId(Cookie cookie){
        return this.extractId(cookie.getValue());
    }

}
