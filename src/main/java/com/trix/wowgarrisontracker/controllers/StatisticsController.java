package com.trix.wowgarrisontracker.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.trix.wowgarrisontracker.converters.AccountToAccountPojo;
import com.trix.wowgarrisontracker.pojos.AccountPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.trix.wowgarrisontracker.utils.JWTutils;

@Controller
public class StatisticsController {

	private GeneralUtils utils;
	private JWTutils jwtUtils;
	private AccountService accountService;
	private AccountToAccountPojo accountToAccountPojo;
	private EntryService entryService;
	
	public StatisticsController(GeneralUtils utils, JWTutils jwtUtils, AccountService accountService,
			AccountToAccountPojo accountToAccountPojo, EntryService entryService) {
		this.utils = utils;
		this.jwtUtils = jwtUtils;
		this.accountService = accountService;
		this.accountToAccountPojo = accountToAccountPojo;
		this.entryService = entryService;
	}

	@GetMapping("statistics")
	public String getStatistics(HttpServletRequest request, Model model) {
		Cookie jwtCookie = utils.extractCookie("Authorization", request.getCookies());
		Long id = jwtUtils.extractId(jwtCookie);
		AccountPojo accountPojo = accountToAccountPojo.convert(accountService.findById(id));
		accountPojo.setDays(Long.valueOf(entryService.getAmountOfDays(id)));
		accountPojo.setAverageGRPerDay(accountPojo.getGarrisonResources()/accountPojo.getDays());
		accountPojo.setAverageWPPerDay(accountPojo.getWarPaint()/accountPojo.getDays());
		model.addAttribute("account", accountPojo);
		return "statistics";
	}

}
