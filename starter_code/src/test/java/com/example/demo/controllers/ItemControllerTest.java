package com.example.demo.controllers;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void testReturnItems() {
        Item firstItem = new Item(1L, "first product", BigDecimal.valueOf(10.00), "first product description");
        Item secondItem = new Item(2L, "second product", BigDecimal.valueOf(130.00), "second product description");
        List<Item> itemList = new ArrayList<>();
        itemList.add(firstItem);
        itemList.add(secondItem);
        when(itemRepo.findAll()).thenReturn(itemList);
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        List<Item> responseItems = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert responseItems != null;
        assertNotNull(responseItems);
        assertEquals(2, responseItems.size());
        assertEquals(firstItem, responseItems.get(0));
        assertEquals(secondItem, responseItems.get(1));
    }

    @Test
    public void testGetItemById() {
        Item thirdItem = new Item(3L, "third product", BigDecimal.valueOf(40.00), "third product description");
        when(itemRepo.findById(3L)).thenReturn(Optional.of(thirdItem));
        ResponseEntity<Item> responseEntity = itemController.getItemById(3L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(thirdItem, responseEntity.getBody());
    }


}