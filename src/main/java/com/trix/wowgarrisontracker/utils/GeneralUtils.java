package com.trix.wowgarrisontracker.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class GeneralUtils {

	private JWTutils jwtUtils;

	public GeneralUtils(JWTutils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}

	public Cookie extractCookie(String cookieName, Cookie[] cookies) {

		for (Cookie tmp : cookies) {
			if (cookieName.equals(tmp.getName()))
				return tmp;
		}
		return null;
	}

	public Long getId(HttpServletRequest httpServletRequest) {
		Cookie cookie = this.extractCookie("Authorization", httpServletRequest.getCookies());
		return jwtUtils.extractId(cookie);
	}

	public Long getId(Cookie [] cookies){
		Cookie cookie = this.extractCookie("Authorization", cookies);
		return jwtUtils.extractId(cookie);
	}
}
