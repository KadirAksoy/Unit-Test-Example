package com.kadiraksoy.UnitTestExample.service;

import com.kadiraksoy.UnitTestExample.converter.UserConverter;
import com.kadiraksoy.UnitTestExample.dto.UserDto;
import com.kadiraksoy.UnitTestExample.exception.UserAlreadyExistException;
import com.kadiraksoy.UnitTestExample.exception.UserNotFoundException;
import com.kadiraksoy.UnitTestExample.model.Todo;
import com.kadiraksoy.UnitTestExample.model.User;
import com.kadiraksoy.UnitTestExample.repository.TodoRepository;
import com.kadiraksoy.UnitTestExample.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private UserService userService;


    @BeforeEach
    void setUp() {
        //Mockito kütüphanesini kullanarak bu test sınıfındaki mock objeleri (sahte nesneler) başlatmak için kullanılır.
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll(){
        List<User> users = new ArrayList<>();

        users.add(new User(1L, "user1", new ArrayList<>()));
        users.add(new User(2L, "user2", new ArrayList<>()));

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(users, result);
    }

    @Test
    void testGetTodosByUserId(){
        User user1 = new User(1L, "user1", new ArrayList<>());
        User user2 = new User(2L, "user2", new ArrayList<>());

        List<Todo> todos = new ArrayList<>();
        todos.add(new Todo(1L, user1, "Todo 1", false, new Date()));
        todos.add(new Todo(2L, user1, "Todo 2", false, new Date()));
        todos.add(new Todo(3L, user2, "Todo 3", true, new Date()));

        when(todoRepository.findAll()).thenReturn(todos);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        List<Todo> result = userService.getTodosByUserId(1L);

        assertEquals(2, result.size());
    }

    @Test
    void testSaveUser(){
        //statik metotlarını (static methods) sahtelemek için kullanılan bir nesne oluşturur.
        MockedStatic<UserConverter> userConverterMockedStatic = Mockito.mockStatic(UserConverter.class);

        UserDto userDto = new UserDto();
        userDto.setUsername("newUser");
        User user = new User(1L, "newUser", new ArrayList<>());

        userConverterMockedStatic.when(() -> UserConverter.toData(Mockito.any())).thenReturn(user);
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        User result = userService.saveUser(userDto);

        assertNotNull(result);
        assertEquals(userDto.getUsername(), result.getUsername());

        userConverterMockedStatic.close();
    }

    @Test
    void testSaveUser_UserAlreadyExists() {
        UserDto userDto = new UserDto();
        userDto.setUsername("existingUser");
        List<User> existingUsers = new ArrayList<>();
        existingUsers.add(new User(1L, "existingUser", new ArrayList<>()));

        when(userRepository.findAll()).thenReturn(existingUsers);

        assertThrows(UserAlreadyExistException.class, () -> userService.saveUser(userDto));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        String username = "updatedUsername";

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(username, 1L));
    }

    @Test
    void testDeleteUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existingUser");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));

        User result = userService.deleteUser(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(existingUser.getUsername(), result.getUsername());
    }

    @Test
    void testGetUserById() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existingUser");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(existingUser.getUsername(), result.getUsername());
    }

    @Test
    void testFindUserById() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existingUser");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));

        User result = userService.findUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(existingUser.getUsername(), result.getUsername());
    }

    @Test
    void testFindUserById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findUserById(1L));
    }
}