package com.kadiraksoy.UnitTestExample.Controller;

import com.kadiraksoy.UnitTestExample.dto.UserDto;
import com.kadiraksoy.UnitTestExample.exception.InvalidUserRequest;
import com.kadiraksoy.UnitTestExample.model.Todo;
import com.kadiraksoy.UnitTestExample.model.User;
import com.kadiraksoy.UnitTestExample.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> retrieveAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserById(userId));
    }

    @GetMapping("/todos/{userId}")
    public ResponseEntity<List<Todo>> retrieveTodosByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.getTodosByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidUserRequest("Invalid user request -> " + userDto.getId());
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.saveUser(userDto));
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody String username, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(username, userId));
    }

    @PostMapping("/delete/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId));
    }
}