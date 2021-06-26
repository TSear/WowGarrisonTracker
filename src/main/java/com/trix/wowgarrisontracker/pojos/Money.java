package com.trix.wowgarrisontracker.pojos;

import java.util.Objects;

public class Money {

    private Long copper;

    public Money() {
        copper = 0L;
    }

    public Money(Long copper) throws RuntimeException {
        if (copper < 0)
            throw new RuntimeException("Copper value cannot be less than 0");
        this.copper = Objects.requireNonNullElse(copper, 0L);
    }

    public Money(int gold, int silver, int copper) throws RuntimeException {

        if (gold < 0 || silver < 0 || copper < 0)
            throw new RuntimeException("Gold and silver and copper value cannot be less than 0");

        this.copper = convertToCopper(gold, silver, copper);
    }

    private Long convertToCopper(int gold, int silver, int copper) {
        long toAdd = gold * 100L + silver;
        toAdd = toAdd * 100L + copper;
        return toAdd;
    }

    public Long getCopperValue() {
        return this.copper;
    }

    public void setCopper(Long copper) throws RuntimeException {
        if (copper < 0)
            throw new RuntimeException("Copper value cannot be less than 0");
        this.copper = copper;
    }

    public boolean addCopper(Long copper) {
        if (copper < 0)
            return false;
        this.copper += copper;
        return true;
    }

    public boolean addSilver(Long silver) {
        if (silver < 0)
            return false;
        this.copper += silver * 100;
        return true;
    }

    public boolean addGold(Long gold) {
        if (gold < 0)
            return false;
        this.copper += gold * 10000;
        return true;
    }

    public void addMoney(int gold, int silver, int copper) {
        this.copper += convertToCopper(gold, silver, copper);
    }

    public void addMoney(Money money) {
        addCopper(money.getCopperValue());
    }

    public int[] getFormattedValues() {
        int[] result = new int[3];
        Long tmp = this.copper;
        result[2] = (int) (tmp % 100);
        tmp /= 100;
        result[1] = (int) (tmp % 100);
        tmp /= 100;
        result[0] = tmp.intValue();
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(copper, money.copper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(copper);
    }

    public Money subtract(Money money) {
        return new Money(this.copper- money.getCopperValue());
    }

    public void subtractMoney(Money moneyFromCards) {
       this.copper -= moneyFromCards.getCopperValue();
    }
}
