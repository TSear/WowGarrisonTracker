package com.trix.wowgarrisontracker.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.CustomUserDetails;
import com.trix.wowgarrisontracker.model.LoginRequest;
import com.trix.wowgarrisontracker.pojos.RegisterModel;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.utils.JWTutils;
import com.trix.wowgarrisontracker.validators.RegisterModelValidator;

import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("account/")
public class AccountController {

	private static final String AUTHORIZATION = "Authorization";
	private AccountService accountService;
	private Logger logger = LoggerFactory.getLogger(Slf4j.class);
	private JWTutils jwTutils;
	private RegisterModelValidator registerModelValidator;

	/**
	 * @param accountService
	 * @param logger
	 * @param jwTutils
	 */
	public AccountController(AccountService accountService, JWTutils jwTutils,
			RegisterModelValidator registerModelValidator) {
		this.registerModelValidator = registerModelValidator;
		this.accountService = accountService;
		this.jwTutils = jwTutils;
	}

	@GetMapping("login/page")
	public String loginPage(Model model) {
		if (!model.containsAttribute("loginRequest")) {
			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setLogin("Login");
			model.addAttribute("loginRequest", loginRequest);
		}
		return "login";
	}

	@PostMapping(value = "login/validate")
	public String login(@ModelAttribute("loginRequest") LoginRequest loginRequest, Model model,
			BindingResult bindingResult, RedirectAttributes redirectAttributes,
			HttpServletResponse httpServletResponse) {

		boolean isPasswordCorrect = false;
		boolean isLoginInDatabase = accountService.isExisting(loginRequest);
		Account account = accountService.findUserByUsername(loginRequest.getLogin());

		if (account != null)
			isPasswordCorrect = loginRequest.getPassword()
					.equals(account.getPassword());

		if (isLoginInDatabase && isPasswordCorrect) {
			logger.info("Procesing credentials");
			// TODO JWT token jest przechowywany w ciasteczku. Będzie trzeba to zmienić na
			// coś bardziej bezpiecznego
			Cookie cookie = new Cookie(AUTHORIZATION, jwTutils.generateToken(new CustomUserDetails(account)));
			cookie.setPath("/");
			cookie.setMaxAge(-1);
			httpServletResponse.addCookie(cookie);
			return "redirect:/infoPage";
		}
		bindingResult.reject("credentials.bad", "Wrong login or password");
		redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.loginRequest",
				bindingResult);
		redirectAttributes.addFlashAttribute("loginRequest", loginRequest);
		return "redirect:/account/login/page";

	}

	@RequestMapping(name = "/account/login/token/refresh")
	public String refreshToken(HttpServletRequest httpServletRequest) {

		Cookie[] cookies = httpServletRequest.getCookies();

		for (Cookie tmpCookie : cookies) {
			if (tmpCookie.getName()
					.equals(AUTHORIZATION)) {

				DefaultClaims claims = (DefaultClaims) httpServletRequest.getAttribute("claims");
				String newToken = jwTutils
						.generateToken(new CustomUserDetails(accountService.findUserByUsername(claims.getSubject())));
				tmpCookie.setValue(newToken);
			}
		}
		logger.info(httpServletRequest.getAttribute("requestUrl")
				.toString());
		return "redirect:/account/get";
	}

	@GetMapping("/register/page")
	public String createAccount(Model model) {
		if(!model.containsAttribute("registerModel")) {
			model.addAttribute("registerModel", new RegisterModel());
		}
		
		return "registerForm";
	}

	@PostMapping("/register/validate")
	public String valideCredentials(@ModelAttribute("registerModel") RegisterModel registerModel, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		registerModelValidator.validate(registerModel, bindingResult);
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("registerModel", registerModel);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerModel",
					bindingResult);
			return "redirect:/account/register/page/";
		}
		Account account = new Account();
		account.setLogin(registerModel.getLogin());
		account.setPassword(registerModel.getPassword());
		accountService.save(account);
		return "redirect:/account/login/page";
	}
}
