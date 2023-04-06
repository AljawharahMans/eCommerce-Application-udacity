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
import java.util.Collections;
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
        itemController = new ItemController(null);
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
        Item item = new Item(1L, "item", BigDecimal.valueOf(42), "item description");

        when(itemRepo.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepo.findByName("item")).thenReturn(Collections.singletonList(item));

    }

    @Test
    public void get_all_items_happy_path() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void get_item_by_id_happy_path() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Item i = response.getBody();
        assertNotNull(i);
    }

    @Test
    public void get_item_by_id_not_found() {
        ResponseEntity<Item> response = itemController.getItemById(2L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_items_by_name_happy_path() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void get_items_by_name_not_found() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Square Widget");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



}