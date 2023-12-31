package com.kadiraksoy.UnitTestExample.Controller;

import com.kadiraksoy.UnitTestExample.dto.UserDto;
import com.kadiraksoy.UnitTestExample.exception.InvalidUserRequest;
import com.kadiraksoy.UnitTestExample.model.Todo;
import com.kadiraksoy.UnitTestExample.model.User;
import com.kadiraksoy.UnitTestExample.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bindingResult = Mockito.mock(BindingResult.class);
    }

    @Test
    void testRetrieveAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "user1", null));
        users.add(new User(2L, "user2", null));

        when(userService.findAll()).thenReturn(users);

        ResponseEntity<List<User>> responseEntity = userController.retrieveAllUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(users, responseEntity.getBody());
    }

    @Test
    void testGetUserById() {
        Long userId = 1L;
        User user = new User(userId, "testUser", null);

        when(userService.findUserById(userId)).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    @Test
    void testRetrieveTodosByUserId() {
        Long userId = 1L;
        List<Todo> todos = new ArrayList<>();
        todos.add(new Todo(1L, null, "Todo 1", false, null));
        todos.add(new Todo(2L, null, "Todo 2", true, null));

        when(userService.getTodosByUserId(userId)).thenReturn(todos);

        ResponseEntity<List<Todo>> responseEntity = userController.retrieveTodosByUserId(userId);

        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        assertEquals(todos, responseEntity.getBody());
    }

    @Test
    void testAddUser_ValidInput() {
        UserDto userDto = new UserDto();
        userDto.setUsername("newUser");

        User savedUser = new User(1L, userDto.getUsername(), null);
        when(userService.saveUser(userDto)).thenReturn(savedUser);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<User> responseEntity = userController.addUser(userDto, bindingResult);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(savedUser, responseEntity.getBody());
    }

    @Test
    void testAddUser_InvalidInput() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        when(bindingResult.hasErrors()).thenReturn(true);

        InvalidUserRequest invalidUserRequest = assertThrows(InvalidUserRequest.class, () -> userController.addUser(userDto, bindingResult));

        assertTrue(invalidUserRequest.getMessage().contains(String.valueOf(userDto.getId())));
        Mockito.verify(userService, never()).saveUser(any(UserDto.class));
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        String newUsername = "updateUsername";

        User updatedUser = new User(userId, "username", null);
        when(userService.updateUser(any(), anyLong())).thenReturn(updatedUser);

        ResponseEntity<User> responseEntity = userController.updateUser(newUsername, userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedUser, responseEntity.getBody());

        verify(userService, times(1)).updateUser(any(), anyLong());
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        User deletedUser = new User(userId, "deletedUser", null);

        when(userService.deleteUser(userId)).thenReturn(deletedUser);

        ResponseEntity<User> responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(deletedUser, responseEntity.getBody());

        Mockito.verify(userService, times(1)).deleteUser(userId);
    }
}