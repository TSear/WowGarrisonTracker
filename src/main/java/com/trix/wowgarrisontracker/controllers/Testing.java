package com.trix.wowgarrisontracker.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trix.wowgarrisontracker.converters.AccountCharacterToAccountCharacterPojo;
import com.trix.wowgarrisontracker.converters.AccountPojoToAccount;
import com.trix.wowgarrisontracker.converters.AccountToAccountPojo;
import com.trix.wowgarrisontracker.converters.AuctionToAuctionPojo;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.model.Auctions;
import com.trix.wowgarrisontracker.model.CustomUserDetails;
import com.trix.wowgarrisontracker.model.ItemEntity;
import com.trix.wowgarrisontracker.model.LoginRequest;
import com.trix.wowgarrisontracker.pojos.AccCharacterResourcesPojo;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.AccountPojo;
import com.trix.wowgarrisontracker.pojos.AuctionHouseInfoPojo;
import com.trix.wowgarrisontracker.pojos.AuctionPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.trix.wowgarrisontracker.services.interfaces.ItemEntityService;
import com.trix.wowgarrisontracker.utils.BlizzardRequestUtils;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.trix.wowgarrisontracker.utils.JWTutils;
import com.trix.wowgarrisontracker.validators.AccountCharacterDTOValidator;
import com.trix.wowgarrisontracker.validators.AccountDTOValidator;
import com.trix.wowgarrisontracker.validators.EntryDTOValidator;
import com.trix.wowgarrisontracker.validators.LoginRequestValidator;

import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("testing/")
@Controller
public class Testing {

	private AccountCharacterService accountCharacterService;
	private AccountService accountService;
	private EntryService entryService;
	private Logger logger = LoggerFactory.getLogger(Slf4j.class);
	private AccountPojoToAccount accountPojoToAccount;
	private AccountToAccountPojo accountToAccountPojo;
	private AccountDTOValidator accountDTOValidator;
	private JWTutils jwTutils;
	private LoginRequestValidator loginRequestValidator;
	private GeneralUtils utils;
	private AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo;
	private EntryDTOValidator entryDTOValidator;
	private AccountCharacterDTOValidator accountCharacterDTOValidator;
	private AuctionService auctionService;
	private ItemEntityService itemEntityService;
	private BlizzardRequestUtils blizzardRequestUtils;
	private AuctionToAuctionPojo auctionToAuctionPojo;

	private final String AUTHORIZATION = "Authorization";

	public Testing(AccountCharacterService accountCharacterService, AccountService accountService,
			EntryService entryService, JWTutils jwTutils, AccountDTOValidator accountDTOValidator,
			LoginRequestValidator loginRequestValidator, GeneralUtils utils,
			AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo,
			AccountCharacterDTOValidator accountCharacterDTOValidator, AuctionService auctionService,
			ItemEntityService itemEntityService, AuctionToAuctionPojo auctionToAuctionPojo) {
		this.accountCharacterService = accountCharacterService;
		this.accountService = accountService;
		this.entryService = entryService;
		this.accountPojoToAccount = new AccountPojoToAccount();
		this.accountToAccountPojo = accountToAccountPojo;
		this.accountDTOValidator = accountDTOValidator;
		this.jwTutils = jwTutils;
		this.loginRequestValidator = loginRequestValidator;
		this.utils = utils;
		this.accountCharacterToAccountCharacterPojo = accountCharacterToAccountCharacterPojo;
		this.entryDTOValidator = new EntryDTOValidator();
		this.accountCharacterDTOValidator = accountCharacterDTOValidator;
		this.auctionService = auctionService;
		this.itemEntityService = itemEntityService;
		this.blizzardRequestUtils = new BlizzardRequestUtils();
		this.auctionToAuctionPojo = auctionToAuctionPojo;
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
			isPasswordCorrect = loginRequest.getPassword().equals(account.getPassword());

		if (isLoginInDatabase && isPasswordCorrect) {
			logger.info("Procesing credentials");
			// TODO JWT token jest przechowywany w ciasteczku. Będzie trzeba to zmienić na
			// coś bardziej bezpiecznego
			Cookie cookie = new Cookie(AUTHORIZATION, jwTutils.generateToken(new CustomUserDetails(account)));
			cookie.setPath("/");
			cookie.setMaxAge(-1);
			httpServletResponse.addCookie(cookie);
			return "redirect:/testing/infoPage";
		}

		bindingResult.reject("credentials.bad", "Wrong login or password");
		redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.loginRequest",
				bindingResult);
		redirectAttributes.addFlashAttribute("loginRequest", loginRequest);
		return "redirect:/testing/login/page";

	}

	@RequestMapping(name = "/testing/login/token/refresh")
	public String refreshToken(HttpServletRequest httpServletRequest) {

		Cookie[] cookies = httpServletRequest.getCookies();

		for (Cookie tmpCookie : cookies) {
			if (tmpCookie.getName().equals(AUTHORIZATION)) {

				DefaultClaims claims = (DefaultClaims) httpServletRequest.getAttribute("claims");
				String newToken = jwTutils
						.generateToken(new CustomUserDetails(accountService.findUserByUsername(claims.getSubject())));
				tmpCookie.setValue(newToken);
			}
		}
		logger.info(httpServletRequest.getAttribute("requestUrl").toString());
		return "/testing/get";
	}

	@ResponseBody
	@PostMapping(value = "login/rest")
	public String restLogin(@RequestBody LoginRequest loginRequest) {
		Account account = accountService.correctCredentials(loginRequest);
		if (account != null) {
			logger.info("Procesing credentials");
			return jwTutils.generateToken(new CustomUserDetails(account));
		}
		return "bad credentials";
	}

	@ResponseBody
	@RequestMapping("get")
	public List<AccountPojo> getObjects() {
		return accountService.findAll();
	}

	@ResponseBody
	@PostMapping("create")
	public String saveAccount(@RequestBody AccountPojo account, BindingResult bindingResult) {
		logger.info("creating");
		accountDTOValidator.validate(account, bindingResult);
		if (!bindingResult.hasErrors()) {
			accountService.save(accountPojoToAccount.convert(account));
			return "saving";
		}
		String errorMessage = "";
		StringBuilder errorMessageBuilder = new StringBuilder(errorMessage);
		for (FieldError tmp : bindingResult.getFieldErrors()) {
			errorMessageBuilder.append(tmp.getDefaultMessage());
			errorMessageBuilder.append("\n");
		}
		errorMessage = errorMessageBuilder.toString();
		return errorMessage;

	}

	@ResponseBody
	@RequestMapping("delete")
	public String deleteAccount(@RequestBody Long id) {

		accountService.delete(id);

		return "asdfasdfasdfasdf";
	}

	@ResponseBody
	@RequestMapping("update")
	public String updateAccount(@Validated @RequestBody AccountPojo account, BindingResult bindingResult, Long id) {

		accountService.update(accountPojoToAccount.convert(account), id);

		return "asdfasdfasdfasdf";
	}



	@GetMapping(value = "infoPage")
	public String getInfoPage() {
		return "infoPage.html";
	}

	@GetMapping(value = "login/logout")
	public String logout(HttpServletRequest servletRequest, HttpServletResponse httpServletResponse) {
		for (Cookie cookie : servletRequest.getCookies()) {
			if ((AUTHORIZATION).equals(cookie.getName())) {
				Cookie cookieExpired = new Cookie(AUTHORIZATION, null);
				cookieExpired.setMaxAge(0);
				cookieExpired.setPath("/");
				httpServletResponse.addCookie(cookieExpired);
			}
		}
		return "redirect:/testing/login/page";
	}


	@GetMapping(value = "/characters")
	public String getCharacters(Model model, HttpServletRequest httpServletRequest) {

		Cookie jwtCookie = utils.extractCookie(AUTHORIZATION, httpServletRequest.getCookies());

		if (jwtCookie != null) {

			Long accountId = jwTutils.extractId(jwtCookie);
			List<AccCharacterResourcesPojo> accResourcesPojoList = accountCharacterService.listOfResources(accountId);

			model.addAttribute("accountCharacters", accResourcesPojoList);
			return "characters";

		}

		return "infoPage";
	}

	@GetMapping(value = "/characters/new")
	public String getCharacterForm(Model model) {

		if (!model.containsAttribute("characterPojo")) {

			model.addAttribute("characterPojo", new AccountCharacterPojo());

		}

		return "characterForm";
	}

	@PostMapping(value = "/characters/validate")
	public String validateNewCharacter(@ModelAttribute("characterPojo") AccountCharacterPojo characterPojo,
			BindingResult bindingResult, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
			Model model) {

		Cookie jwtCookie = utils.extractCookie(AUTHORIZATION, httpServletRequest.getCookies());

		if (jwtCookie != null) {

			characterPojo.setAccountId(jwTutils.extractId(jwtCookie));
			accountCharacterDTOValidator.validate(characterPojo, bindingResult);

			if (!bindingResult.hasErrors()) {
				accountCharacterService.save(characterPojo);
				return "redirect:/testing/characters";
			} else {
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.characterPojo",
						bindingResult);
				redirectAttributes.addFlashAttribute("characterPojo", characterPojo);
				return "redirect:/testing/characters/new";
			}
		}

		return "redirect:/testing/characters";

	}

	@GetMapping(value = "/auctionhouse/get")
	public String getAuctionHouse(Model model) {

		List<ItemEntity> itemList = itemEntityService.findAllItemEntities();
		List<AuctionPojo> returnAuctionList = new ArrayList<>();

		for (ItemEntity itemTmp : itemList) {
			List<AuctionEntity> listOfAuctionEntities = auctionService.getAuctionsByItemId(itemTmp.getId());
			AuctionPojo auctionPojoTmp = new AuctionPojo();
			auctionPojoTmp.setItemName(itemTmp.getName());
			for (AuctionEntity auctionEntityTmp : listOfAuctionEntities) {
				AuctionHouseInfoPojo auctionHouseInfoPojo = new AuctionHouseInfoPojo();
				auctionHouseInfoPojo.setQuantity(auctionEntityTmp.getQuantity());
				auctionHouseInfoPojo.setUnitPrice(auctionEntityTmp.getUnitPrice());
				auctionPojoTmp.getInfo().add(auctionHouseInfoPojo);
			}
			returnAuctionList.add(auctionPojoTmp);
			// returnAuctionList.add(listOfAuctionEntities.stream().map(auctionToAuctionPojo::convert).collect(Collectors.toList()));
		}
		returnAuctionList.stream().forEach(item -> item.getInfo().sort(
				(AuctionHouseInfoPojo o1, AuctionHouseInfoPojo o2) -> o1.getUnitPrice().compareTo(o2.getUnitPrice())));

		model.addAttribute("items", returnAuctionList);

		return "auctionHouse.html";
	}

}