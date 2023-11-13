package com.kadiraksoy.UnitTestExample.service;


import com.kadiraksoy.UnitTestExample.converter.UserConverter;
import com.kadiraksoy.UnitTestExample.dto.UserDto;
import com.kadiraksoy.UnitTestExample.exception.UserAlreadyExistException;
import com.kadiraksoy.UnitTestExample.exception.UserNotFoundException;
import com.kadiraksoy.UnitTestExample.model.Todo;
import com.kadiraksoy.UnitTestExample.model.User;
import com.kadiraksoy.UnitTestExample.repository.TodoRepository;
import com.kadiraksoy.UnitTestExample.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    public UserService(UserRepository userRepository, TodoRepository todoRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public List<Todo> getTodosByUserId(Long userId){
        User user = isUserExists(userId);
        List<Todo> todos = todoRepository.findAll()
                .stream()
                .filter(e -> e.getUser().getId().equals(userId)).toList();
        return todos;
    }

    public User saveUser(UserDto userDto) {
        List<User> users = userRepository.findAll();
        if (users.stream().anyMatch(u -> u.getUsername().equals(userDto.getUsername()))) {
            log.error("User already exists with username: {}", userDto.getUsername());
            throw new UserAlreadyExistException("User Already Exists!");
        }
        User user = UserConverter.toData(userDto);
        userRepository.save(user);
        return user;
    }

    public User updateUser(String username, Long userId) {
        User user = isUserExists(userId);
        user.setUsername(username);
        userRepository.save(user);
        log.info("User updated successfully: {} (ID: {})", username, userId);
        return user;
    }

    public User getUserById(Long userId) {
        User user = isUserExists(userId);
        return user;
    }

    public User deleteUser(Long userId) {
        User user = isUserExists(userId);
        userRepository.deleteById(userId);
        log.info("User deleted successfully: {} (ID: {})", user.getUsername(), userId);
        return user;
    }

    public User findUserById(Long userId) {
        User user = isUserExists(userId);
        return user;
    }


    private User isUserExists(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (Objects.isNull(user)) {
            log.error("User not found with ID: {}", userId);
            throw new UserNotFoundException(String.format("There is no user with the given id => {}", userId));
        }
        return user;
    }
}
