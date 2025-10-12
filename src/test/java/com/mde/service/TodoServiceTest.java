package com.mde.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mde.entity.TodoEntity;
import com.mde.type.TodoStatusType;

public class TodoServiceTest {

	private TodoService todoService;

	@Before
	public void setUp() {
		todoService = new TodoService();
	}

	@Test
	public void addAssignsIdAndPersistsEntity() {
		TodoEntity entity = new TodoEntity();
		entity.setName("Learn Testing");
		entity.setStatus(TodoStatusType.OPEN);

		boolean added = todoService.add(entity);

		assertTrue(added);
		assertTrue(entity.getId() > 0);
		List<TodoEntity> all = todoService.getAll();
		assertEquals(1, all.size());
		assertEquals(entity.getId(), all.get(0).getId());
		assertEquals(TodoStatusType.OPEN, all.get(0).getStatus());
	}

	@Test
	public void addRejectsDuplicateNameIgnoringCase() {
		TodoEntity entity = new TodoEntity();
		entity.setName("Learn Testing");
		todoService.add(entity);

		TodoEntity duplicate = new TodoEntity();
		duplicate.setName("learn testing");

		assertFalse(todoService.add(duplicate));
		assertEquals(1, todoService.getAll().size());
	}

	@Test
	public void editUpdatesExistingEntity() {
		TodoEntity entity = new TodoEntity();
		entity.setName("Learn Testing");
		todoService.add(entity);

		TodoEntity updated = new TodoEntity();
		updated.setId(entity.getId());
		updated.setName("Learn Mockito");
		updated.setStatus(TodoStatusType.CLOSED);

		assertTrue(todoService.edit(updated));
		TodoEntity stored = todoService.getAll().get(0);
		assertEquals("Learn Mockito", stored.getName());
		assertEquals(TodoStatusType.CLOSED, stored.getStatus());
	}

	@Test
	public void deleteRemovesEntityWhenPresent() {
		TodoEntity entity = new TodoEntity();
		entity.setName("Learn Testing");
		todoService.add(entity);

		boolean deleted = todoService.delete(entity.getId());
		assertTrue(deleted);
		assertTrue(todoService.getAll().isEmpty());
	}

	@Test
	public void saveDelegatesToAdd() {
		TodoEntity entity = new TodoEntity();
		entity.setName("Learn Delegation");

		assertTrue(todoService.save(entity));
		assertFalse(todoService.save(entity));
	}
}
