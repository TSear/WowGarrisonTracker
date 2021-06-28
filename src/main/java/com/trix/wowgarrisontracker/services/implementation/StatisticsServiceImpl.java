package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.pojos.Money;
import com.trix.wowgarrisontracker.pojos.StatisticsPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.CardsOfOmenService;
import com.trix.wowgarrisontracker.services.interfaces.StatisticsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final AccountService accountService;
    private final CardsOfOmenService cardsOfOmenService;

    public StatisticsServiceImpl(AccountService accountService, CardsOfOmenService cards) {
        this.accountService = accountService;
        this.cardsOfOmenService = cards;
    }

    @Override
    public StatisticsPojo getAllStatistics(Long accountId) {
        Account account = accountService.findById(accountId);
        StatisticsPojo statistics = new StatisticsPojo();
        Long days = accountService.countDays(account.getId());
        Long garrisonResources =Objects.requireNonNullElse(accountService.sumEntriesGarrisonResources(account.getId()),0L);
        Long totalWarPaint = Objects.requireNonNullElse(accountService.sumEntriesWarPaint(account.getId()),0L);


        statistics.setAmountOfEntries(accountService.countEntries(account.getId()).intValue());
        statistics.setDays(days.intValue());
        statistics.setAverageDailyGarrisonResources((int) (garrisonResources / Math.max(days,1)));
        statistics.setAverageDailyWarPaint((int) (totalWarPaint / Math.max(days,1)));
        statistics.setTotalGarrisonResources(garrisonResources);
        statistics.setTotalWarPaint(totalWarPaint);
        statistics.setTotalGold(new Money(cardsOfOmenService.sumTotalGold(account.getId())).toString());
        statistics.setTotalCardsOpened(cardsOfOmenService.sumTotalOpenedCards(account.getId()));

        return statistics;
    }
}
