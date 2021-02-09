package com.trix.wowgarrisontracker.controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.trix.wowgarrisontracker.converters.AccountCharacterToAccountCharacterPojo;
import com.trix.wowgarrisontracker.converters.AccountPojoToAccount;
import com.trix.wowgarrisontracker.converters.AccountToAccountPojo;
import com.trix.wowgarrisontracker.deserializers.AuctionHouseResponseDeserializer;
import com.trix.wowgarrisontracker.deserializers.ItemEntityResponseDeserializer;
import com.trix.wowgarrisontracker.deserializers.ItemWrapperDeserializer;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.model.Auctions;
import com.trix.wowgarrisontracker.model.CustomUserDetails;
import com.trix.wowgarrisontracker.model.ItemEntity;
import com.trix.wowgarrisontracker.model.LoginRequest;
import com.trix.wowgarrisontracker.pojos.AccCharacterResourcesPojo;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.AccountPojo;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
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
import com.trix.wowgarrisontracker.wrapers.ItemsWraper;

import io.jsonwebtoken.Claims;
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

	private final String AUTHORIZATION = "Authorization";

	public Testing(AccountCharacterService accountCharacterService, AccountService accountService,
			EntryService entryService, JWTutils jwTutils, AccountDTOValidator accountDTOValidator,
			LoginRequestValidator loginRequestValidator, GeneralUtils utils,
			AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo,
			AccountCharacterDTOValidator accountCharacterDTOValidator,
			AuctionService auctionService, ItemEntityService itemEntityService) {
		this.accountCharacterService = accountCharacterService;
		this.accountService = accountService;
		this.entryService = entryService;
		this.accountPojoToAccount = new AccountPojoToAccount();
		this.accountToAccountPojo = new AccountToAccountPojo();
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

	@RequestMapping("track")
	public String getTrackingPage(HttpServletRequest httpServletRequest, Model model) {
		Claims claims = jwTutils.extractClaims(
				utils.extractCookie(AUTHORIZATION, httpServletRequest.getCookies()).getValue().substring(7));
		Long id = Long.valueOf((int) claims.get("accountId"));
		model.addAttribute("entries", accountService.getAllEntries(id));
		return "track";
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

	@GetMapping(value = "track/newEntry")
	public String getEntryForm(Model model, HttpServletRequest httpServletRequest) {

		Cookie cookie = utils.extractCookie(AUTHORIZATION, httpServletRequest.getCookies());

		if (cookie != null) {

			List<AccountCharacterPojo> accountCharacterPojoList = new ArrayList<>();
			Long id = jwTutils.extractId(cookie);

			for (AccountCharacter tmp : accountCharacterService.listOfAccountCharacters(id)) {
				accountCharacterPojoList.add(accountCharacterToAccountCharacterPojo.convert(tmp));
			}
			
			if(!model.containsAttribute("entry")) {				
				model.addAttribute("entry", new EntryPojo());
			}
			model.addAttribute("characters", accountCharacterPojoList);

			return "entryForm";
		}

		return "track";
	}

	@PostMapping(value = "track/validate")
	public String validateNewEntry(@ModelAttribute("entry") EntryPojo entry, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		//TODO coś się zjebało. Jak poda się pustą wartość w formie to kontroller odbiera to jako pustego stringa i wywala error.
		entryDTOValidator.validate(entry, bindingResult);

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.entry", bindingResult);
			redirectAttributes.addFlashAttribute("entry", entry);
			return "redirect:/testing/track/newEntry";
		}

		entryService.saveEntry(entry);

		return "redirect:/testing/track";
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
	
	@GetMapping(value = "/auctionhouse/update")
	public String updateAuctionHouse() {
		
		try {
			
			
//			
//			ObjectMapper auctionMapper = new ObjectMapper();
//			String commandForAh = "curl -X GET https://eu.api.blizzard.com/data/wow/connected-realm/3713/auctions?namespace=dynamic-eu&locale=en_US&access_token="+mappedString.get("access_token");
//			Process process2 = Runtime.getRuntime().exec(commandForAh);
//			InputStream auctionHouseInput = process2.getInputStream();
//			String ahText = new BufferedReader(
//					new InputStreamReader(auctionHouseInput, StandardCharsets.UTF_8))
//					.lines()
//					.collect(Collectors.joining("\n"));
//			SimpleModule module = new SimpleModule("AuctionHouseResponseDeserializer", new Version(1, 0, 0, null, null, null));
//			module.addDeserializer(AuctionEntity.class, new AuctionHouseResponseDeserializer());
//			auctionMapper.registerModule(module);
//			auctionMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//			Auctions ah = auctionMapper.readValue(ahText, Auctions.class);
//			
//			ah.getAuctions().forEach(auctionService::save);
			
//			String requestForItems = "curl -X GET https://us.api.blizzard.com/data/wow/search/item?namespace=static-us&locale=en_US&orderby=id&_page=1&access_token=" +mappedString.get("access_token");
//			Process process3 = Runtime.getRuntime().exec(requestForItems);
//			InputStream itemsInput = process3.getInputStream();
//			String itemsJsonText = new BufferedReader(
//				      new InputStreamReader(itemsInput, StandardCharsets.UTF_8))
//				        .lines()
//				        .collect(Collectors.joining("\n"));
//			ObjectMapper itemMapper = new ObjectMapper();
//			itemMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			itemMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//			ItemsWraper itemsResponse = itemMapper.readValue(itemsJsonText, ItemsWraper.class);
//			System.out.println(itemsResponse);
//			itemsResponse.getResults().forEach(itemEntityService::save);
//			
//			for(int i = 2 ; i < itemsResponse.getPages(); i++) {
//				String request = "curl -X GET https://us.api.blizzard.com/data/wow/search/item?namespace=static-us&locale=en_US&orderby=id&_page="+i+"&access_token=" +mappedString.get("access_token");
//				Process pr = Runtime.getRuntime().exec(request);
//				InputStream items = pr.getInputStream();
//				String itemsJson = new BufferedReader(
//					      new InputStreamReader(items, StandardCharsets.UTF_8))
//					        .lines()
//					        .collect(Collectors.joining("\n"));
//				ObjectMapper itemMapper2 = new ObjectMapper();
//				itemMapper2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//				itemMapper2.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//				ItemsWraper itemsResponse2 = itemMapper.readValue(itemsJson, ItemsWraper.class);
//				System.out.println(itemsResponse2);
//				itemsResponse2.getResults().forEach(itemEntityService::save);
//			}
			
			ItemsWraper auctionHouseWrapper = blizzardRequestUtils.getWowItems(1);
			auctionHouseWrapper.getResults().forEach(itemEntityService::save);
			
			for(int i = 2 ; i <= auctionHouseWrapper.getPages(); i++) {
				ItemsWraper anotherPage = blizzardRequestUtils.getWowItems(i);
				anotherPage.getResults().forEach(itemEntityService::save);
			}
			
			Auctions auctions = blizzardRequestUtils.getAuctionHouse();
			System.out.println(auctions.getAuctions().toString());
			
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "redirect:/testing/auctionhouse/get";
	}
	
	@GetMapping(value = "/auctionhouse/get")
	public String getAuctionHouse() {
		try {
//			String command = "curl -u b0ea73a8d8f74e9b949e5c9868911b7f:C6LomejtnBU682JAdzOKMpZ7t5htKwvL -d grant_type=client_credentials https://us.battle.net/oauth/token";
//			Process process = Runtime.getRuntime().exec(command);
//			InputStream inputStream = process.getInputStream();
//			String text = new BufferedReader(
//				      new InputStreamReader(inputStream, StandardCharsets.UTF_8))
//				        .lines()
//				        .collect(Collectors.joining("\n"));
//			System.out.println(text);
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			Map<String, Object> mappedString = mapper.readValue(text, new TypeReference<Map<String, Object>>(){});
//			String token = mappedString.get("token_type")+" "+mappedString.get("access_token");
//			System.out.println(token);
			

			
			//TODO trzeba zrobić pobieranie nazw itemkow od blizzarda, nastepnie zrobic zapisywanie ich w bazie w jakiejś uproszczonej formie
//			

//			
//			
//			
		}catch(Exception e ) {
			System.out.println(e.getMessage());
		}
		return "infoPage";
	}

}