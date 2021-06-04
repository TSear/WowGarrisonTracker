package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.pojos.Statistics;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.StatisticsService;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final AccountService accountService;

    public StatisticsServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Long getTotalGarrisonResources(Long accountId) {
        Account account = accountService.findById(accountId);
        return account.getTotalGarrisonResources();
    }

    @Override
    public Long getTotalWarPaint(Long accountId) {
        Account account = accountService.findById(accountId);
        return account.getTotalWarPaint();
    }

    @Override
    public int getAverageWarPaintPerDay(Long accountId) {
        Account account = accountService.findById(accountId);
        return (int) (account.getTotalWarPaint() / 1);
    }

    @Override
    public int getAverageGarrisonResourcesPerDay(Long accountId) {
        Account account = accountService.findById(accountId);
        return (int) (account.getTotalGarrisonResources() / 1);
    }

    @Override
    public int getAmountOfEntries(Long accountId) {
        Account account = accountService.findById(accountId);
        return account.getAmountOfEntries();
    }

    @Override
    public int getAmountOfDays(Long accountId) {
        return 1;
    }

    @Override
    public Statistics getAllStatistics(Long accountId) {
        Account account = accountService.findById(accountId);
        Statistics statistics = new Statistics();

        statistics.setAmountOfEntries(account.getAmountOfEntries());
        statistics.setDays(1);
        statistics.setAverageDailyGarrisonResources((int) (account.getTotalGarrisonResources() / 1));
        statistics.setAverageDailyWarPaint((int) (account.getTotalWarPaint() / 1));
        statistics.setTotalGarrisonResources(account.getTotalGarrisonResources());
        statistics.setTotalWarPaint(account.getTotalWarPaint());

        return statistics;
    }
}
