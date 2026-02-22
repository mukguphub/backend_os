/**
 * 
 */
package com.mde.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mde.entity.TodoEntity;
import com.mde.type.TodoStatusType;

/**
 * @author mukeshgupta
 *
 */

@RunWith(org.junit.runners.JUnit4.class)
public class TodoControllerTest {

	private MockMvc mockMvc;
	private StubTodoService todoService;

	@Before
	public void setUp() {
		todoService = new StubTodoService();
		TodoController controller = new TodoController(todoService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setValidator(new NoOpValidator())
				.build();
	}

	private TodoController todoController;



	/**
	 * Test method for {@link com.mde.controller.TodoController#getAll()}.
	 * 
	 * @throws Exception
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetAll() throws URISyntaxException, Exception {
		List<TodoEntity> entities = new ArrayList<TodoEntity>();
		TodoEntity first = new TodoEntity();
		first.setId(1);
		first.setName("First Task");
		first.setStatus(TodoStatusType.OPEN);
		TodoEntity second = new TodoEntity();
		second.setId(2);
		second.setName("Make Demo");
		second.setStatus(TodoStatusType.CLOSED);

		entities.add(first);
		entities.add(second);

		todoService.setTodos(entities);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/todo")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		JSONAssert.assertEquals("[{id:1,name:\"First Task\"},{id:2,name:\"Make Demo\"}]",
				result.getResponse().getContentAsString(), false);
	}

	@Test
	public void testProcessCreatesDefaultTodo() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/save"))
				.andReturn();

		TodoEntity saved = todoService.getSavedEntity();
		org.junit.Assert.assertEquals("Learn Spring Boot", saved.getName());
		org.junit.Assert.assertEquals(TodoStatusType.OPEN, saved.getStatus());
		org.junit.Assert.assertEquals(200, result.getResponse().getStatus());
		org.junit.Assert.assertEquals("Saved", result.getResponse().getContentAsString());
	}

	@Test
	public void testAddCreatesTodo() throws Exception {
		todoService.setAddOutcome(true, 10L);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/new")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"New Task\",\"status\":\"OPEN\"}"))
				.andReturn();

		org.junit.Assert.assertEquals(201, result.getResponse().getStatus());
		JSONAssert.assertEquals("{id:10,name:\"New Task\"}", result.getResponse().getContentAsString(), false);
	}

	@Test
	public void testAddDuplicateReturnsConflict() throws Exception {
		todoService.setAddOutcome(false, 0L);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/new")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"New Task\"}"))
				.andReturn();

		org.junit.Assert.assertEquals(409, result.getResponse().getStatus());
	}

	@Test
	public void testDeleteNotFound() throws Exception {
		todoService.setDeleteOutcome(false);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/delete/5"))
				.andReturn();

		org.junit.Assert.assertEquals(404, result.getResponse().getStatus());
	}

	private static class StubTodoService extends com.mde.service.TodoService {

		private List<TodoEntity> todos = new ArrayList<TodoEntity>();
		private boolean addResult = true;
		private long assignedId = 0L;
		private boolean deleteResult = true;
		private TodoEntity savedEntity;

		void setTodos(List<TodoEntity> todos) {
			this.todos = todos;
		}

		void setAddOutcome(boolean result, long idToAssign) {
			this.addResult = result;
			this.assignedId = idToAssign;
		}

		void setDeleteOutcome(boolean result) {
			this.deleteResult = result;
		}

		TodoEntity getSavedEntity() {
			return savedEntity;
		}

		@Override
		public List<TodoEntity> getAll() {
			return todos;
		}

		@Override
		public boolean add(TodoEntity entity) {
			if (addResult && assignedId > 0) {
				entity.setId(assignedId);
			}
			return addResult;
		}

		@Override
		public boolean delete(long id) {
			return deleteResult;
		}

		@Override
		public boolean save(TodoEntity todoEntity) {
			savedEntity = todoEntity;
			return true;
		}
	}

	private static class NoOpValidator implements Validator {

		@Override
		public boolean supports(Class<?> clazz) {
			return true;
		}

		@Override
		public void validate(Object target, Errors errors) {
			// No-op for tests
		}
	}
}
