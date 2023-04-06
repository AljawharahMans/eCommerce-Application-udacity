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
import org.springframework.http.HttpStatus;
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
    private OrderController orderController;
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController(null, null);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        Item item = new Item(1L, "item", BigDecimal.valueOf(89), "item description");
        List<Item> items = new ArrayList<Item>();
        items.add(item);
        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("testPassword");
        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(2.99);
        cart.setTotal(total);
        user.setCart(cart);
        when(userRepo.findByUsername("test")).thenReturn(user);
        when(userRepo.findByUsername("someone")).thenReturn(null);

    }

    @Test
    public void submit_order_happy_path() {
        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
    }

    @Test
    public void submit_order_user_not_found() {
        ResponseEntity<UserOrder> response = orderController.submit("someone");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_orders_for_user_happy_path() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("test");
        assertNotNull(ordersForUser);
        assertEquals(HttpStatus.OK, ordersForUser.getStatusCode());
        List<UserOrder> orders = ordersForUser.getBody();
        assertNotNull(orders);

    }

    @Test
    public void get_orders_for_user_not_found() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("someone");
        assertNotNull(ordersForUser);
        assertEquals(HttpStatus.NOT_FOUND, ordersForUser.getStatusCode());

    }
}