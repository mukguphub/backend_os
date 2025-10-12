package com.mde.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mde.entity.TodoEntity;
import com.mde.type.TodoStatusType;

@Service
public class TodoService {

   private final ConcurrentMap<Long, TodoEntity> todoEntities = new ConcurrentHashMap<>();
   private final AtomicLong idSequence = new AtomicLong();

    public List<TodoEntity> getAll() {
        return todoEntities.values().stream()
                .sorted(Comparator.comparingLong(TodoEntity::getId))
                .map(this::copy)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean add(TodoEntity entity) {
    	if (entity == null) {
    		return false;
    	}

    	TodoEntity candidate = prepare(entity);
    	if (hasDuplicateName(candidate.getName(), -1)) {
    		return false;
    	}

    	ensureId(candidate);
    	entity.setId(candidate.getId());

    	return todoEntities.putIfAbsent(candidate.getId(), candidate) == null;
    }

    public boolean edit(TodoEntity entity) {
        if (entity == null || entity.getId() <= 0) {
            return false;
        }

        TodoEntity candidate = prepare(entity);
        if (hasDuplicateName(candidate.getName(), candidate.getId())) {
            return false;
        }

        return todoEntities.computeIfPresent(candidate.getId(), (id, existing) -> candidate) != null;
    }

    public boolean delete(long id) {
    	return todoEntities.remove(id) != null;
    }

	public boolean save(TodoEntity todoEntity) {
		return add(todoEntity);
	}

	private TodoEntity prepare(TodoEntity entity) {
		TodoEntity prepared = new TodoEntity();
		prepared.setId(entity.getId());
		prepared.setName(entity.getName());
		prepared.setStatus(entity.getStatus() != null ? entity.getStatus() : TodoStatusType.OPEN);
		return prepared;
	}

	private void ensureId(TodoEntity entity) {
		if (entity.getId() <= 0) {
			entity.setId(idSequence.incrementAndGet());
		}
	}

	private boolean hasDuplicateName(String name, long ignoreId) {
		if (name == null) {
			return false;
		}
		return todoEntities.values().stream()
				.anyMatch(todo -> todo.getId() != ignoreId && name.equalsIgnoreCase(todo.getName()));
	}

	private TodoEntity copy(TodoEntity entity) {
		TodoEntity copy = new TodoEntity();
		copy.setId(entity.getId());
		copy.setName(entity.getName());
		copy.setStatus(entity.getStatus());
		return copy;
	}

}
