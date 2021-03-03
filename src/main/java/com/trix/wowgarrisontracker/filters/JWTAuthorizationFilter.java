package com.trix.wowgarrisontracker.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.trix.wowgarrisontracker.enums.SecurityValues;
import com.trix.wowgarrisontracker.utils.JWTutils;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@PropertySource("classpath:configuration.yaml")
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private String KEY = "M.Ds9ZR}C4q1WDvcxnD57H.Og([Ep";
	private Logger logger = LoggerFactory.getLogger(Slf4j.class);
	private AuthenticationManager authenticationManager;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Optional<Cookie> optionalJwtCookie = Arrays.asList(request.getCookies())
				.stream()
				.filter(item -> item.getName()
						.equals(SecurityValues.AUTHRORIZATION.toString()))
				.findFirst();
		logger.info(request.getRequestURI());
		logger.info(SecurityValues.AUTHRORIZATION.toString());
		Cookie jwtCookie = null;
		if (optionalJwtCookie.isPresent()) {
			jwtCookie = optionalJwtCookie.get();
		}
		if (jwtCookie == null || !jwtCookie.getValue()
				.startsWith(SecurityValues.BEARER.toString())) {
			chain.doFilter(request, response);
			return;
		}

		UsernamePasswordAuthenticationToken usernameToken = getAuthenticated(jwtCookie);
		SecurityContextHolder.getContext()
				.setAuthentication(usernameToken);

		super.doFilterInternal(request, response, chain);
	}

	private UsernamePasswordAuthenticationToken getAuthenticated(Cookie jwtCookie) {
		if (jwtCookie != null) {
			String user = Jwts.parser()
					.setSigningKey(KEY)
					.parseClaimsJws(jwtCookie.getValue()
							.substring(7))
					.getBody()
					.getSubject();
			if (user != null) {

				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			return null;
		}
		return null;
	}

}
