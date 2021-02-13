package com.trix.wowgarrisontracker.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trix.wowgarrisontracker.converters.AccountCharacterToAccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.trix.wowgarrisontracker.utils.JWTutils;
import com.trix.wowgarrisontracker.validators.EntryDTOValidator;

import io.jsonwebtoken.Claims;

@RequestMapping(value = "/testing/track")
@Controller
public class TrackController {

	private static final String AUTHORIZATION = "Authorization";
	private JWTutils jwtUtils;
	private GeneralUtils utils;
	private AccountCharacterService accountCharacterService;
	private AccountCharacterToAccountCharacterPojo accountCharacterToPojo;
	private EntryDTOValidator entryDTOValidator;
	private AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo;
	private EntryService entryService;
	private AccountService accountService;
	

	/**
	 * @param jwtUtils
	 * @param utils
	 * @param accountCharacterService
	 * @param accountCharacterToPojo
	 * @param entryDTOValidator
	 * @param accountCharacterToAccountCharacterPojo
	 * @param entryService
	 */
	public TrackController(JWTutils jwtUtils, GeneralUtils utils, AccountCharacterService accountCharacterService,
			AccountCharacterToAccountCharacterPojo accountCharacterToPojo,
			AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo, EntryService entryService,
			AccountService accountService) {
		this.jwtUtils = jwtUtils;
		this.utils = utils;
		this.accountCharacterService = accountCharacterService;
		this.accountCharacterToPojo = accountCharacterToPojo;
		this.entryDTOValidator = new EntryDTOValidator();
		this.accountCharacterToAccountCharacterPojo = accountCharacterToAccountCharacterPojo;
		this.entryService = entryService;
		this.accountService = accountService;
	}
	
	@RequestMapping("")
	public String getTrackingPage(HttpServletRequest httpServletRequest, Model model) {
		Claims claims = jwtUtils.extractClaims(
				utils.extractCookie(AUTHORIZATION, httpServletRequest.getCookies()).getValue().substring(7));
		
		Long id = Long.valueOf((int) claims.get("accountId"));
		model.addAttribute("entries", accountService.getAllEntries(id));
		return "track";
	}
	
	@GetMapping(value = "newEntry")
	public String getEntryForm(Model model, HttpServletRequest httpServletRequest) {

		Cookie cookie = utils.extractCookie(AUTHORIZATION, httpServletRequest.getCookies());

		if (cookie != null) {

			Long id = jwtUtils.extractId(cookie);

			List<AccountCharacterPojo> accountCharacterPojoList = accountCharacterService.listOfAccountCharacters(id)
					.stream()
					.map(accountCharacterToAccountCharacterPojo::convert)
					.collect(Collectors.toList());

			if (!model.containsAttribute("entry")) {
				model.addAttribute("entry", new EntryPojo());
			}

			model.addAttribute("characters", accountCharacterPojoList);

			return "entryForm";
		}

		return "track";
	}

	@PostMapping(value = "validate")
	public String validateNewEntry(@ModelAttribute("entry") EntryPojo entry, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		entryDTOValidator.validate(entry, bindingResult);

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.entry", bindingResult);
			redirectAttributes.addFlashAttribute("entry", entry);
			return "redirect:/testing/track/newEntry";
		}

		entryService.saveEntry(entry);

		return "redirect:/testing/track";
	}

}
