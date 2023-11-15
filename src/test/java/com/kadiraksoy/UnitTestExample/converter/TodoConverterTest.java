package com.kadiraksoy.UnitTestExample.converter;

import com.kadiraksoy.UnitTestExample.dto.TodoDto;
import com.kadiraksoy.UnitTestExample.exception.UserNotFoundException;
import com.kadiraksoy.UnitTestExample.model.Todo;
import com.kadiraksoy.UnitTestExample.model.User;
import com.kadiraksoy.UnitTestExample.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//bu test sınıfının Mockito kütüphanesini kullanarak mock nesneleri ve
// mock işlemleri için genişletileceğini belirtir.
@ExtendWith(MockitoExtension.class)
class TodoConverterTest {

    @Mock
    private UserRepository userRepository;

    private TodoDto todoDto;
    private User user;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        todoDto = new TodoDto();
        todoDto.setComplete(false);
        todoDto.setUserId(1L);
        todoDto.setDescription("testDescription");

        user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
    }

    @DisplayName("Convert todoDto to Todo with valid user")
    @Test
    void toData_validUser(){
        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(Mockito.anyLong());
        Todo todo = TodoConverter.toData(todoDto, userRepository);

        Assertions.assertNotNull(todo);
        Assertions.assertEquals("testDescription", todo.getDescription());
        Assertions.assertFalse(todo.isComplete());
        Assertions.assertEquals(1L, todo.getUser().getId());
        Assertions.assertEquals("testUsername", todo.getUser().getUsername());

    }

    @DisplayName("Convert todoDto to Todo with invalid user. Expect throw error")
    @Test
    void toData_invalidUser() {
        Mockito.doReturn(Optional.empty()).when(userRepository).findById(Mockito.anyLong());

        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> {
            TodoConverter.toData(todoDto, userRepository);
        });

        Assertions.assertTrue(userNotFoundException.getMessage().contains(String.valueOf(todoDto.getUserId())));
    }
}