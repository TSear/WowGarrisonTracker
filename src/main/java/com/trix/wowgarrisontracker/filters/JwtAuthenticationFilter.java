package com.trix.wowgarrisontracker.filters;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.trix.wowgarrisontracker.enums.SecurityValues;
import com.trix.wowgarrisontracker.utils.JWTutils;

import lombok.extern.slf4j.Slf4j;

//TODO Trzeba doczytać co tu się dzieje
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private JWTutils utils;
	private int jwtCookieMaxAge = 60000;

	
	public JwtAuthenticationFilter(AuthenticationManager auth, JWTutils utils) {
		super(auth);
		this.authenticationManager = auth;
		this.utils = utils;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {

			String login = request.getParameter("login");
			String password = obtainPassword(request);
			return authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(login, password, new ArrayList<>()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String login = request.getParameter("login");
		String token = utils.generateToken(login);
		Cookie jwtCookie = new Cookie(SecurityValues.AUTHRORIZATION.toString(), token);
		jwtCookie.setMaxAge(-1);
		jwtCookie.setPath("/**");
		response.addCookie(jwtCookie);
	}

}
