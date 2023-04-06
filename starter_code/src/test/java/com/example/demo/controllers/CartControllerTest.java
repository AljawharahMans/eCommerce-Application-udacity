package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);

        Item item = new Item(1L, "item", BigDecimal.valueOf(22), "description for item");
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
    }

    @Test
    public void addToCartHappyPath() {
        ModifyCartRequest request = new ModifyCartRequest("test", 1L, 1);
        ResponseEntity<Cart> response = cartController.addToCart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(BigDecimal.valueOf(22), cart.getTotal());

    }

    @Test
    public void whenUserNotFoundAddItemToCartReturnsNotFound() {
        ModifyCartRequest request = new ModifyCartRequest("not-saved-user", 1, 5);
        ResponseEntity<Cart> response = cartController.addToCart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenItemNotFoundAddItemToCartReturnsNotFound() {
        ModifyCartRequest request = new ModifyCartRequest("test", 2, 7);
        ResponseEntity<Cart> response = cartController.addToCart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void removeFromCartHappyPath() {
        ModifyCartRequest request = new ModifyCartRequest("test", 1L, 7);
        ResponseEntity<Cart> response = cartController.addToCart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        request = new ModifyCartRequest("test", 1L, 5);
        response = cartController.removeFromCart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(BigDecimal.valueOf(44), cart.getTotal());

    }

    @Test
    public void whenUserNotFoundRemoveItemFromCartReturnsNotFound() {
        ModifyCartRequest request = new ModifyCartRequest("not-saved-user", 3, 2);
        ResponseEntity<Cart> response = cartController.removeFromCart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenItemNotFoundRemoveItemFromCartReturnsNotFound() {
        ModifyCartRequest request = new ModifyCartRequest("test", 4, 14);
        ResponseEntity<Cart> response = cartController.removeFromCart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
