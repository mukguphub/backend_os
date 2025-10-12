package com.mde.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mde.entity.TodoEntity;
import com.mde.service.TodoService;
import com.mde.type.TodoStatusType;



@RestController
@CrossOrigin
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/save")
    public ResponseEntity<String> process(){ 
    	TodoEntity todoEntity = new TodoEntity();
    	todoEntity.setStatus(TodoStatusType.OPEN);
    	todoEntity.setName("Learn Spring Boot");
		if (todoService.save(todoEntity)) {
			return ResponseEntity.ok("Saved");
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Todo already exists");
    }
   
    @RequestMapping(method = RequestMethod.GET, path = "/todo")
    public ResponseEntity<List<TodoEntity>> getAll(){
        return ResponseEntity.ok(todoService.getAll());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/new")
    public ResponseEntity<TodoEntity> add(@Valid @RequestBody TodoEntity entity){
        if (todoService.add(entity)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(entity);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/edit")
    public ResponseEntity<TodoEntity> edit(@Valid @RequestBody TodoEntity entity){
        if (todoService.edit(entity)) {
            return ResponseEntity.ok(entity);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

 
    @RequestMapping(method = RequestMethod.DELETE, path = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id){
        if (todoService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

}
