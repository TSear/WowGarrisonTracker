package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.blizzarapi.BlizzardApiRequests;
import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.model.ItemEntity;
import com.trix.wowgarrisontracker.pojos.AuctionPojoWrapper;
import com.trix.wowgarrisontracker.repository.AuctionEntityRepository;
import com.trix.wowgarrisontracker.repository.ItemEntityRepository;
import com.trix.wowgarrisontracker.services.interfaces.ItemsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuctionServiceImplTest {

    @Mock
    ItemEntityRepository itemEntityRepository;

    @Mock
    AuctionEntityRepository auctionEntityRepository;

    @Mock
    ItemsService itemsService;

    @Mock
    BlizzardApiRequests blizzardApiRequests;

    @InjectMocks
    AuctionServiceImpl auctionService;

    List<ItemEntity> items;

    @BeforeEach
    void setUp() {
        items = new ArrayList<>();
        ItemEntity itemEntity1 = new ItemEntity(100L, "Test1");
        ItemEntity itemEntity2 = new ItemEntity(200L, "Test2");
        ItemEntity itemEntity3 = new ItemEntity(300L, "Test3");
        items.addAll(Arrays.asList(itemEntity1,itemEntity2,itemEntity3));
    }

    @Test
    void findByConnectedServerId_successfully() {
        //given
        List<AuctionEntity> notFlattened = new ArrayList<>();
        notFlattened.add(new AuctionEntity(1L,100L,5000L,40L,5, items.get(0)));
        notFlattened.add(new AuctionEntity(2L,100L,5500L,200L,5, items.get(0)));
        notFlattened.add(new AuctionEntity(3L,100L,5000L,40L,5, items.get(0)));
        notFlattened.add(new AuctionEntity(4L,200L,9000L,40L,5, items.get(1)));
        notFlattened.add(new AuctionEntity(5L,300L,5000L,40L,5, items.get(2)));
        notFlattened.add(new AuctionEntity(6L,200L,5900L,60L,5, items.get(1)));
        notFlattened.add(new AuctionEntity(7L,200L,9000L,40L,5, items.get(1)));

        //when
        when(auctionEntityRepository.findByConnectedServerId(Mockito.anyInt())).thenReturn(notFlattened);
        when(itemsService.findAll()).thenReturn(items);
        List<AuctionPojoWrapper> returned = auctionService.findByConnectedServerIdPojo(Mockito.anyInt());

        //then
        assertEquals(3, returned.size());
    }

}