/**
 * 
 */
package com.mde.controller;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
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
import com.mde.service.TodoService;

/**
 * @author mukeshgupta
 *
 */

public class TodoControllerTest {

	private MockMvc mockMvc;

	private TodoService todoService;

	private TodoController todoController;

	@Before
	public void setup() {
		todoService = new TodoService();
		todoController = new TodoController();
		todoController.todoService = todoService;
		mockMvc = MockMvcBuilders.standaloneSetup(todoController)
				.setValidator(new NoOpValidator())
				.build();
	}

	/**
	 * Test method for {@link com.mde.controller.TodoController#getAll()}.
	 * 
	 * @throws Exception
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetAll() throws URISyntaxException, Exception {
		
		TodoEntity entity = new TodoEntity();
		entity.setId(1);
		TodoEntity entity2 = new TodoEntity();
		entity2.setId(2);
		entity2.setName("Make Demo");
		
		todoService.add(entity);
		todoService.add(entity2);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/todo").contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{id:1},{id:2,name:\"Make Demo\"}]";

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);

	}

	private static class NoOpValidator implements Validator {

		@Override
		public boolean supports(Class<?> clazz) {
			return false;
		}

		@Override
		public void validate(Object target, Errors errors) {
			// intentionally blank
		}
	}
}
