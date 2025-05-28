package com.lseg.todolist.task.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lseg.todolist.task.application.createtask.CreationTask;
import com.lseg.todolist.task.application.createtask.CreationTaskCommand;
import com.lseg.todolist.task.domain.entity.Status;
import com.lseg.todolist.task.domain.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskRestController.class)
class TaskRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreationTask creationTask;

    @Test
    @DisplayName("Should create task and return 201 status")
    void shouldCreateTaskAndReturn201Status() throws Exception {
        // Given
        var command = new CreationTaskCommand(
                "Test Task",
                "Test Description",
                LocalDate.now()
        );

        var expectedTask = Task.builder()
                .id(UUID.randomUUID())
                .title(command.title())
                .description(command.description())
                .dueDate(command.dueDate())
                .status(Status.PENDING)
                .build();

        when(creationTask.executeCreationTask(any(CreationTaskCommand.class)))
                .thenReturn(expectedTask);

        // When / Then
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(command.title()))
                .andExpect(jsonPath("$.description").value(command.description()))
                .andExpect(jsonPath("$.status").value(Status.PENDING.name()));
    }
}