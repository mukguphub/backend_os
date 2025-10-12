package com.mde.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mde.entity.TodoEntity;
import com.mde.service.TodoService;

@RestController
@CrossOrigin
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/save")
    public String process() {
        // TODO in Class 2
        return "Not Done. This is a black Impl...";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/todo")
    public List<TodoEntity> getAll() {
        return todoService.getAll();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/new")
    public boolean add(@Valid @RequestBody TodoEntity entity) {
        return todoService.add(entity);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/edit")
    public boolean edit(@Valid @RequestBody TodoEntity entity) {
        return todoService.edit(entity);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/delete/{id}")
    public boolean delete(@PathVariable("id") long id) {
        return todoService.delete(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public boolean handleValidationException(MethodArgumentNotValidException exception) {
        return false;
    }
}
