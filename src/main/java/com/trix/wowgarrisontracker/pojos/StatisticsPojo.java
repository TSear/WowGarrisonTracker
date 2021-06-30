package com.trix.wowgarrisontracker.pojos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticsPojo {

    private int amountOfEntries;
    private int days;
    private int averageDailyGarrisonResources;
    private int averageDailyWarPaint;
    private Long totalWarPaint;
    private Long totalGarrisonResources;
    private String totalGold;
    private Long totalCardsOpened;

}
