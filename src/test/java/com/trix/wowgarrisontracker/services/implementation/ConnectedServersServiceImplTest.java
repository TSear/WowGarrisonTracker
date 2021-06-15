package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.ConnectedServersModel;
import com.trix.wowgarrisontracker.repository.ConnectedServersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ConnectedServersServiceImplTest {

    @Mock
    ConnectedServersRepository repository;

    @InjectMocks
    ConnectedServersServiceImpl connectedServersService;

    List<ConnectedServersModel> servers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        ConnectedServersModel model1 = new ConnectedServersModel();
        model1.setConnectedServerId(1);

        ConnectedServersModel model2 = new ConnectedServersModel();
        model2.setConnectedServerId(2);

        ConnectedServersModel model3 = new ConnectedServersModel();
        model3.setConnectedServerId(3);

        ConnectedServersModel model4 = new ConnectedServersModel();
        model4.setConnectedServerId(4);

        ConnectedServersModel model5 = new ConnectedServersModel();
        model5.setConnectedServerId(5);

        servers.addAll(Arrays.asList(model1,model2,model3,model4,model5));

    }

    @Test
    void save_success() {
        //given
        ConnectedServersModel connectedServersModel = new ConnectedServersModel();
        connectedServersModel.setConnectedServerId(1);

        //when
        when(repository.save(Mockito.any(ConnectedServersModel.class))).thenReturn(connectedServersModel);

        //then
        assertTrue(connectedServersService.save(connectedServersModel));
    }

    @Test
    void save_fail() {
        //given
        //when
        //then
        assertFalse(connectedServersService.save(null));
    }

    @Test
    void saveAll_success() {
        //given
        //when
        when(repository.saveAll(Mockito.any())).thenReturn(servers);
        //then
        assertTrue(connectedServersService.saveAll(servers));
    }

    @Test
    void saveAll_fail() {
        //given
        //when
        //then
        assertFalse(connectedServersService.saveAll(null));
    }
}