package com.trix.wowgarrisontracker.config;

import com.trix.wowgarrisontracker.filters.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.trix.wowgarrisontracker.filters.JWTAuthorizationFilter;
import com.trix.wowgarrisontracker.filters.JwtAuthenticationFilter;
import com.trix.wowgarrisontracker.services.implementation.AccountDetailsService;
import com.trix.wowgarrisontracker.utils.JWTutils;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Autowired
	private AccountDetailsService accountDetailsService;
	@Autowired
	private JWTutils utils;

	@Autowired
	private JWTFilter jwtFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(accountDetailsService)
				.passwordEncoder(passwordEncoder());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.antMatchers("/h2-console/**")
				.antMatchers("/resources/**")
				.antMatchers("/bootstrapCss/**")
				.antMatchers("/bootstrapJs/**")
				.antMatchers("/css/**")
				.antMatchers("/img/**")
				.antMatchers("/js/**");

//        web.ignoring().antMatchers("/account/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
				.disable();

//		http.authorizeRequests()
//				.antMatchers("/account/login/**")
//				.permitAll()
//
//				.antMatchers("/resources/**")
//				.permitAll()
//				.antMatchers("/css/**")
//				.permitAll()
//				.antMatchers("/img/**")
//				.permitAll()
//				.antMatchers("/js/**")
//				.permitAll()
//				.anyRequest()
//				.authenticated()
//				.and()
//				.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//				.and()
//				.formLogin()
//				.loginPage("/account/login/page")
//				.loginProcessingUrl("/account/login/validate")
//				.defaultSuccessUrl("/infoPage", true);

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
				.loginProcessingUrl("/account/login/verify");
//				.defaultSuccessUrl("/testing/get", true);
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//		http.addFilter(jwtAuthenticationFilter());
//		http.addFilter(jwtAuthorizationFilter());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

//	@Bean
//	public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
//		JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager(), utils);
//		filter.setFilterProcessesUrl("/account/login/test");
//		return filter;
//	}
//
//	public JWTAuthorizationFilter jwtAuthorizationFilter() throws Exception {
//		return new JWTAuthorizationFilter(authenticationManager());
//	}

}