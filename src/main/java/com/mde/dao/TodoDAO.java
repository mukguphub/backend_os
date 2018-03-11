package com.mde.dao;

import java.util.List;

import com.mde.entity.TodoEntity;

public interface TodoDAO {
    TodoEntity findById(long id);
    TodoEntity findByName(String name);
    List<TodoEntity> findAll();
    TodoEntity saveOrUpdate(TodoEntity entity);
    void delete(TodoEntity entity);
    void delete(long id);
	void save(TodoEntity todoEntity);
}
