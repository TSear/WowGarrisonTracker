package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.pojos.Statistics;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.StatisticsService;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private AccountService accountService;

    public StatisticsServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Long getTotalGarrisonResources(Long accountId) {
        Account account = accountService.findById(accountId);
        return account.getTotalGarrisonResources();
    }

    @Override
    public int getTotalWarPaint(Long accountId) {
        return 0;
    }

    @Override
    public int getAverageWarPaintPerDay(Long accountId) {
        return 0;
    }

    @Override
    public int getAverageGarrisonResourcesPerDay(Long accountId) {
        return 0;
    }

    @Override
    public int getAmountOfEntries(Long accountId) {
        return 0;
    }

    @Override
    public int getAmountOfDays(Long accountId) {
        return 0;
    }

    @Override
    public Statistics getAllStatistics(Long accountId) {
        return null;
    }
}
