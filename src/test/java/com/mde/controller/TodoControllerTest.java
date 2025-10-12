package com.mde.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.mde.entity.TodoEntity;
import com.mde.service.TodoService;
import com.mde.type.TodoStatusType;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Test
    void testGetAll() throws URISyntaxException, Exception {
        List<TodoEntity> entities = new ArrayList<>();
        TodoEntity entity = new TodoEntity();
        entity.setId(1);
        TodoEntity entity2 = new TodoEntity();
        entity2.setId(2);
        entity2.setName("Make Demo");

        entities.add(entity2);
        entities.add(entity);

        Mockito.when(todoService.getAll()).thenReturn(entities);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/todo")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        String expected = "[{id:1},{id:2,name:\"Make Demo\"}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void testAdd() throws Exception {
        Mockito.when(todoService.add(Mockito.any(TodoEntity.class))).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"Task\",\"status\":\"OPEN\"}");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<TodoEntity> captor = ArgumentCaptor.forClass(TodoEntity.class);
        Mockito.verify(todoService).add(captor.capture());

        TodoEntity captured = captor.getValue();
        assertEquals(1, captured.getId());
        assertEquals("Task", captured.getName());
        assertEquals(TodoStatusType.OPEN, captured.getStatus());
        assertEquals("true", result.getResponse().getContentAsString());
    }

    @Test
    void testEdit() throws Exception {
        Mockito.when(todoService.edit(Mockito.any(TodoEntity.class))).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":2,\"name\":\"Updated Task\",\"status\":\"CLOSED\"}");

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<TodoEntity> captor = ArgumentCaptor.forClass(TodoEntity.class);
        Mockito.verify(todoService).edit(captor.capture());

        TodoEntity captured = captor.getValue();
        assertEquals(2, captured.getId());
        assertEquals("Updated Task", captured.getName());
        assertEquals(TodoStatusType.CLOSED, captured.getStatus());
        assertEquals("true", result.getResponse().getContentAsString());
    }

    @Test
    void testDelete() throws Exception {
        Mockito.when(todoService.delete(3L)).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/delete/{id}", 3L)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        Mockito.verify(todoService).delete(3L);
        assertEquals("true", result.getResponse().getContentAsString());
    }
}
