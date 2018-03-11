/**
 * 
 */
package com.mde.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.mde.entity.TodoEntity;
import com.mde.service.TodoService;

/**
 * @author mukeshgupta
 *
 */

@RunWith(SpringRunner.class)
@WebMvcTest(value = TodoController.class, secure = false)
public class TodoControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TodoService todoService;

	/**
	 * Test method for {@link com.mde.controller.TodoController#getAll()}.
	 * 
	 * @throws Exception
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetAll() throws URISyntaxException, Exception {
		
		List<TodoEntity> entities = new ArrayList<TodoEntity>();
		TodoEntity entity = new TodoEntity();
		entity.setId(1);
		TodoEntity entity2 = new TodoEntity();
		entity2.setId(2);
		entity2.setName("Make Demo");
		
		entities.add(entity2);
		entities.add(entity);
		
		Mockito.when(
				todoService.getAll()).thenReturn(entities);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/todo").contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{id:1},{id:2,name:\"Make Demo\"}]";

		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
		
	}
}
