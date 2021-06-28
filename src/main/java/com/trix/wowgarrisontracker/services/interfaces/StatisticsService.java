package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.pojos.StatisticsPojo;

public interface StatisticsService {

    StatisticsPojo getAllStatistics(Long accountId);

}
