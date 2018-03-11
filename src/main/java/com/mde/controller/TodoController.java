package com.mde.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.mde.entity.TodoEntity;
import com.mde.service.TodoService;
import com.mde.type.TodoStatusType;

import javax.validation.Valid;
import java.util.List;



@RestController
@CrossOrigin
public class TodoController {

    @Autowired
    TodoService todoService;
    
    @RequestMapping(method = RequestMethod.GET, path = "/save")
    public String process(){ 
    	todoService.save(new TodoEntity("my first todo item", TodoStatusType.OPEN));
    	todoService.save(new TodoEntity("my first todo item_2", TodoStatusType.CLOSED));
    	todoService.save(new TodoEntity("my first todo item_3", TodoStatusType.OPEN));
		return "Done";
    }
    @RequestMapping(method = RequestMethod.GET, path = "/todo")
    public List<TodoEntity> getAll(){
        return todoService.getAll();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/new")
    public boolean add(@Valid @RequestBody TodoEntity entity){
        return todoService.add(entity);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/edit")
    public boolean edit(@Valid @RequestBody TodoEntity entity){
        return todoService.edit(entity);
    }

 
    @RequestMapping(method = RequestMethod.DELETE, path = "/delete/{id}")
    public boolean delete(@PathVariable("id") long id){
        return todoService.delete(id);
    }

    
    @ExceptionHandler
    public boolean handleValidationException(MethodArgumentNotValidException exception){
        return false;
    }

}