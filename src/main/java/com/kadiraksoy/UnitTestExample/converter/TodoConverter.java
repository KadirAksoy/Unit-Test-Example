package com.kadiraksoy.UnitTestExample.converter;

import com.kadiraksoy.UnitTestExample.dto.TodoDto;
import com.kadiraksoy.UnitTestExample.exception.UserNotFoundException;
import com.kadiraksoy.UnitTestExample.model.Todo;
import com.kadiraksoy.UnitTestExample.model.User;
import com.kadiraksoy.UnitTestExample.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TodoConverter {

    public static Todo toData(TodoDto todoDto, UserRepository userRepository) {
        Todo todo = new Todo();
        todo.setComplete(todoDto.isComplete());
        todo.setDescription(todoDto.getDescription());

        User user = userRepository.findById(todoDto.getUserId()).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("The user for the given Id could not be found -> " + todoDto.getUserId());
        }

        todo.setUser(user);
        return todo;
    }
}