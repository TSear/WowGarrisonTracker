package com.trix.wowgarrisontracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OptionsTest {

    Options options;
    Server server;

    @BeforeEach
    public void init(){
        options = new Options();
        server = new Server();
    }

    @Test
    void getServerName(){
        server.setName("test1");
        options.setServer(server);

        assertEquals("test1", options.getServer().getName());

        server.setName("test2");

        assertNotEquals("test1", options.getServer().getName());
    }

    @Test
    void receiveNotifications(){
        options.setReceiveEmailNotifications(false);

        assertFalse(options.isReceiveEmailNotifications());

        options.setReceiveEmailNotifications(true);

        assertTrue(options.isReceiveEmailNotifications());
    }

}