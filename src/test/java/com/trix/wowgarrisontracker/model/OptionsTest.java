package com.trix.wowgarrisontracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OptionsTest {

    Options options;

    @BeforeEach
    public void init(){
        options = new Options();
    }

    @Test
    void getServerName(){
        options.setServerName("test1");

        assertEquals("test1", options.getServerName());

        options.setServerName("test2");

        assertNotEquals("test1", options.getServerName());
    }

    @Test
    void receiveNotifications(){
        options.setReceiveEmailNotifications(false);

        assertFalse(options.isReceiveEmailNotifications());

        options.setReceiveEmailNotifications(true);

        assertTrue(options.isReceiveEmailNotifications());
    }

}