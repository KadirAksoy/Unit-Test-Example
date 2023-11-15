package com.kadiraksoy.UnitTestExample.converter;

import com.kadiraksoy.UnitTestExample.dto.UserDto;
import com.kadiraksoy.UnitTestExample.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

    private UserDto userDto;

    @BeforeEach
    void setUp() {

        userDto = new UserDto();
        userDto.setUsername("testUsername");
        userDto.setId(1L);
    }

    @Test
    void toData(){
        User user = UserConverter.toData(userDto);
        Assertions.assertEquals("testUsername",user.getUsername());
        Assertions.assertEquals(1L, user.getId());
    }
}