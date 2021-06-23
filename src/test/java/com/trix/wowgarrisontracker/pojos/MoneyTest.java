package com.trix.wowgarrisontracker.pojos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MoneyTest {


    @Test
    void constructor_correctConversion() {
        //given
        Money money = new Money(100,10,10);
        Money moneyConversion = new Money(1001010L);

        //when
        boolean equals = money.equals(moneyConversion);

        //then
        assertTrue(equals);
    }

    @Test
    void constructor_incorrectValuesException() {
        //given

        //when

        //then
        assertThrows(RuntimeException.class,() -> new Money(-50,0,0));
        assertThrows(RuntimeException.class,() -> new Money(0,-150,0));
        assertThrows(RuntimeException.class,() -> new Money(0,0,-150));
        assertThrows(RuntimeException.class,() -> new Money(-150L));

    }

    @Test
    void getTotalInCopper() {
        //given
        Money money = new Money(100,10,10);

        //when

        //then
        assertEquals(1001010L,money.getCopperValue());
    }

    @Test
    void convert() {
        //given
        Money money = new Money();

        //when
        money.addMoney(100,10,10);

        //then
        assertEquals(1001010, money.getCopperValue());
    }

    @Test
    void addCopper() {
        //given
        Money money = new Money();
        //when
        boolean result = money.addCopper(50L);

        //then
        assertEquals(50,money.getCopperValue());
        assertTrue(result);
    }

    @Test
    void addCopper_fail() {
        //given
        Money money = new Money();

        //when
        boolean result = money.addCopper(-50L);

        //then
        assertFalse(result);
        assertEquals(0,money.getCopperValue());
    }

    @Test
    void addSilver() {
        //given
        Money money = new Money();
        //when
        boolean result = money.addSilver(50L);

        //then
        assertEquals(5000,money.getCopperValue());
        assertTrue(result);
    }

    @Test
    void addSilver_fail() {
        //given
        Money money = new Money();

        //when
        boolean result = money.addSilver(-50L);

        //then
        assertFalse(result);
        assertEquals(0,money.getCopperValue());
    }

    @Test
    void addGold() {
        //given
        Money money = new Money();
        //when
        boolean result = money.addGold(50L);

        //then
        assertEquals(500000,money.getCopperValue());
        assertTrue(result);
    }

    @Test
    void addGold_fail() {
        //given
        Money money = new Money();

        //when
        boolean result = money.addGold(-50L);

        //then
        assertFalse(result);
        assertEquals(0,money.getCopperValue());
    }

    @Test
    void getFormattedValues() {
        //given
        Money money = new Money(1001010L);

        //when
        int [] values = money.getFormattedValues();

        //then
        assertEquals(100, values[0]);
        assertEquals(10,values[1]);
        assertEquals(10,values[1]);


    }
}