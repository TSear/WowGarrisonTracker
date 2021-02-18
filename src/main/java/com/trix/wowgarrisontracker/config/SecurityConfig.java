package com.trix.wowgarrisontracker.config;

import com.trix.wowgarrisontracker.filters.JWTFilter;
import com.trix.wowgarrisontracker.services.implementation.AccountDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AccountDetailsService accountDetailsService;

	private JWTFilter jwtFilter;

	public SecurityConfig(JWTFilter jwtFilter) {
		this.jwtFilter = jwtFilter;

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(accountDetailsService);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.antMatchers("/h2-console/**");
//        web.ignoring().antMatchers("/account/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
				.disable();

		http.authorizeRequests()
				.antMatchers("/account/**")
				.permitAll()
				.antMatchers("/account/login/*")
				.permitAll()
				.antMatchers("/css/*")
				.permitAll()
				.antMatchers("/img/**")
				.permitAll()
				.antMatchers("/js/*")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.formLogin()
				.loginPage("/account/login/page")
				.loginProcessingUrl("/account/login/verify")
				.defaultSuccessUrl("/testing/get", true);

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}