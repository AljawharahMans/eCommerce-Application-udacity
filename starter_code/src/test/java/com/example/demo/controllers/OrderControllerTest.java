package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepo;

    @Mock
    private OrderRepository orderRepository;
    @Before
    public void setUp(){
        Item firstItem = new Item(1L, "first product", BigDecimal.valueOf(10.00), "first product description");
        List<Item> itemList = Collections.singletonList(firstItem);
        User user = new User();
        Cart cart = new Cart();
        user.setId(1);
        user.setUsername("aljawharah");
        user.setPassword("aA123");
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(itemList);
        cart.setTotal(BigDecimal.valueOf(100));
        user.setCart(cart);
        when(userRepo.findByUsername("aljawharah")).thenReturn(user);
        when(userRepo.findByUsername("notFoundUser")).thenReturn(null);
    }

    @Test
    public void testSubmitOrder() {
        ResponseEntity<UserOrder> responseEntity = orderController.submit("aljawharah");
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        UserOrder order = responseEntity.getBody();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
        responseEntity = orderController.submit("notFoundUser");
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testGetOrdersForUser() {
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("aljawharah");
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        List<UserOrder> orders = responseEntity.getBody();
        assertNotNull(orders);
        responseEntity = orderController.getOrdersForUser("notFoundUser");
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

}