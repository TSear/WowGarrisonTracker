package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.pojos.StatisticsPojo;

public interface StatisticsService {

    Long getTotalGarrisonResources(Long accountId);

    Long getTotalWarPaint(Long accountId);

    int getAverageWarPaintPerDay(Long accountId);

    int getAverageGarrisonResourcesPerDay(Long accountId);

    int getAmountOfEntries(Long accountId);

    int getAmountOfDays(Long accountId);

    StatisticsPojo getAllStatistics(Long accountId);

}
