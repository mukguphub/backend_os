package com.mde.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mde.entity.TodoEntity;

@Service
public class TodoService {

   List<TodoEntity> todoEntities = new ArrayList<>();
	
    public List<TodoEntity> getAll() {
        return todoEntities;
    }

    public boolean add(TodoEntity entity) {
    	return todoEntities.add(entity);
    }

    public boolean edit(TodoEntity entity) {
        for(TodoEntity todoEntity : todoEntities){
        	if(todoEntity.getId() == entity.getId()){
        		if(todoEntities.remove(todoEntity)){
        			todoEntities.add(entity);
        			return true;
        		}
        		
        	}
        }
        return false;
    }

    public boolean delete(long id) {
    	 for(TodoEntity todoEntity : todoEntities){
         	if(todoEntity.getId() == id){
         		return todoEntities.remove(todoEntity);
         	}
         }
        return false;
    }

	public void save(TodoEntity todoEntity) {
		//TODO : To check for duplicate at this time.
		todoEntities.add(todoEntity);
		return;
	}

}
