package com.kadiraksoy.UnitTestExample.converter;

import com.kadiraksoy.UnitTestExample.dto.UserDto;
import com.kadiraksoy.UnitTestExample.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public static User toData(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        return user;
    }
}