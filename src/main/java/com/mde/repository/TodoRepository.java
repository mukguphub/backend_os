package com.mde.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.mde.entity.TodoEntity;

@Component

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    TodoEntity findByName(String name);
}
