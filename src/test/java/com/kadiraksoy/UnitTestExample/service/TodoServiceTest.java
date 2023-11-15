package com.kadiraksoy.UnitTestExample.service;

import com.kadiraksoy.UnitTestExample.converter.TodoConverter;
import com.kadiraksoy.UnitTestExample.dto.TodoDto;
import com.kadiraksoy.UnitTestExample.exception.UserNotFoundException;
import com.kadiraksoy.UnitTestExample.model.Todo;
import com.kadiraksoy.UnitTestExample.model.User;
import com.kadiraksoy.UnitTestExample.repository.TodoRepository;
import com.kadiraksoy.UnitTestExample.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TodoService todoService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindTodoById() {
        Todo existingTodo = new Todo(1L, new User(1L, "user1", new ArrayList<>()),
                "Todo 1", false, new Date());

        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(existingTodo));

        Todo result = todoService.findTodoById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testAddTodo() {
        MockedStatic<TodoConverter> todoConverterMockedStatic = Mockito.mockStatic(TodoConverter.class);
        TodoDto todoDto = new TodoDto();
        todoDto.setDescription("Todo 1");
        todoDto.setUserId(1L);
        User existingUser = new User(1L, "user1", new ArrayList<>());
        Todo todo = new Todo(1L, existingUser, "Todo 1", false, new Date());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        todoConverterMockedStatic.when(() -> TodoConverter.toData(Mockito.any(TodoDto.class), Mockito.any())).thenReturn(todo);

        Todo result = todoService.addTodo(todoDto);

        assertNotNull(result);
        assertEquals(todoDto.getDescription(), result.getDescription());
        assertEquals(existingUser, result.getUser());

        todoConverterMockedStatic.close();
    }

    @Test
    void testAddTodo_UserNotFound() {
        TodoDto todoDto = new TodoDto();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> todoService.addTodo(todoDto));

    }

    @Test
    void testDeleteTodo() {
        User user = new User(1L, "user1", new ArrayList<>());
        Todo existingTodo = new Todo(1L, user, "Task 1", false, new Date());

        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(existingTodo));

        Todo result = todoService.deleteTodo(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testSwitchTodoStatus_TodoIsNotCompleted() {
        User user = new User(1L, "user1", new ArrayList<>());
        Todo existingTodo = new Todo(1L, user, "Task 1", false, new Date());

        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(existingTodo));

        Todo result = todoService.switchTodoStatus(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(result.isComplete());

        Mockito.verify(todoRepository, times(1)).save(result);
    }

    @Test
    void testSwitchTodoStatus_TodoIsCompleted() {
        User user = new User(1L, "user1", new ArrayList<>());
        Todo existingTodo = new Todo(1L, user, "Task 1", true, new Date());

        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(existingTodo));

        Todo result = todoService.switchTodoStatus(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertFalse(result.isComplete());

        Mockito.verify(todoRepository, times(1)).save(result);
    }

    @Test
    void testFindAll() {
        List<Todo> todos = new ArrayList<>();
        User user = new User(1L, "user1", new ArrayList<>());
        todos.add(new Todo(1L, user, "Task 1", false, new Date()));
        todos.add(new Todo(2L, user, "Task 2", true, new Date()));
        todos.add(new Todo(3L, user, "Task 3", false, new Date()));

        when(todoRepository.findAll()).thenReturn(todos);

        List<Todo> result = todoService.findAll();

        assertNotNull(result);
        assertEquals(todos.size(), result.size());
        assertEquals(todos, result);
    }

    @Test
    void testGetTodosByUserId() {
        User user1 = new User(1L, "user1", new ArrayList<>());
        User user2 = new User(2L, "user2", new ArrayList<>());
        List<Todo> todos = new ArrayList<>();
        todos.add(new Todo(1L, user1, "Task 1", false, new Date()));
        todos.add(new Todo(2L, user1, "Task 2", false, new Date()));
        todos.add(new Todo(3L, user2, "Task 3", false, new Date()));

        when(todoRepository.findAll()).thenReturn(todos);

        List<Todo> result = todoService.getTodosByUserId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(todo -> todo.getUser().getId().equals(1L)));
    }
}