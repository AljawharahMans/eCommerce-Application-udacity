package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPath() {
        when(encoder.encode(any())).thenReturn("thisIsHashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("aljawharah");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");
        ResponseEntity response = userController.createUser(request);
        assertNotNull(Objects.requireNonNull(response.getBody()));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void createUserPasswordTooShort() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("aljawharah");
        request.setPassword("Aa123");
        request.setConfirmPassword("Aa123");
        ResponseEntity responseEntity = userController.createUser(request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void createUserPasswordMismatch() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("aljawharah");
        request.setPassword("Aa123");
        request.setConfirmPassword("Aa123");
        ResponseEntity responseEntity = userController.createUser(request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void findUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("aljawharah");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<User> responseEntity = userController.findById(1L);
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    @Test
    public void findUserByUsername() {
        User user = new User();
        user.setId(1L);
        user.setUsername("aljawharah");
        when(userRepository.findByUsername("aljawharah")).thenReturn(user);
        ResponseEntity<User> responseEntity = userController.findByUserName("aljawharah");
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    @Test
    public void findUserByUsernameNotFound() {
        when(userRepository.findByUsername("aljawharah")).thenReturn(null);
        ResponseEntity<User> responseEntity = userController.findByUserName("aljawharah");
        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}
