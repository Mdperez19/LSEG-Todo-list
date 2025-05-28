package com.lseg.todolist.task.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lseg.todolist.task.application.createtask.CreationTask;
import com.lseg.todolist.task.application.createtask.CreationTaskCommand;
import com.lseg.todolist.task.application.readalltasks.TaskReader;
import com.lseg.todolist.task.application.readtask.TaskDetailReader;
import com.lseg.todolist.task.domain.entity.Status;
import com.lseg.todolist.task.domain.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskRestController.class)
class TaskRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreationTask creationTask;

    @MockitoBean
    private TaskReader taskReader;

    @MockitoBean
    private TaskDetailReader taskDetailReader;


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

    @Test
    @DisplayName("Should return page of tasks with 200 status")
    void should_return_page_of_tasks_with_200_status() throws Exception {
        // Given
        List<Task> tasks = List.of(
                Task.builder()
                        .id(UUID.randomUUID())
                        .title("First Task")
                        .description("First Description")
                        .dueDate(LocalDate.now().plusDays(1))
                        .status(Status.PENDING)
                        .build(),
                Task.builder()
                        .id(UUID.randomUUID())
                        .title("Second Task")
                        .description("Second Description")
                        .dueDate(LocalDate.now().plusDays(2))
                        .status(Status.IN_PROGRESS)
                        .build()
        );

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(tasks, pageable, tasks.size());

        when(taskReader.executeTaskReader(any(PageRequest.class))).thenReturn(taskPage);

        // When / Then
        mockMvc.perform(get("/tasks")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("First Task"))
                .andExpect(jsonPath("$.content[0].description").value("First Description"))
                .andExpect(jsonPath("$.content[0].status").value("PENDING"))
                .andExpect(jsonPath("$.content[1].title").value("Second Task"))
                .andExpect(jsonPath("$.content[1].description").value("Second Description"))
                .andExpect(jsonPath("$.content[1].status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    @DisplayName("Should return empty page when no tasks exist")
    void should_return_empty_page_when_no_tasks_exist() throws Exception {
        // Given
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Task> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(taskReader.executeTaskReader(any(PageRequest.class))).thenReturn(emptyPage);

        // When / Then
        mockMvc.perform(get("/tasks")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    @DisplayName("Should return task with 200 status when task exists")
    void should_return_task_with_200_status_when_task_exists() throws Exception {
        // Given
        UUID taskId = UUID.randomUUID();
        Task task = Task.builder()
                .id(taskId)
                .title("Test Task")
                .description("Test Description")
                .dueDate(LocalDate.now().plusDays(1))
                .status(Status.PENDING)
                .build();

        when(taskDetailReader.executeTaskDetailReader(taskId))
                .thenReturn(Optional.of(task));

        // When / Then
        mockMvc.perform(get("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Should return 404 status when task does not exist")
    void should_return_404_status_when_task_does_not_exist() throws Exception {
        // Given
        UUID nonExistentTaskId = UUID.randomUUID();
        when(taskDetailReader.executeTaskDetailReader(nonExistentTaskId))
                .thenReturn(Optional.empty());

        // When / Then
        mockMvc.perform(get("/tasks/{id}", nonExistentTaskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 status when invalid UUID is provided")
    void should_return_400_status_when_invalid_uuid_is_provided() throws Exception {
        // Given
        String invalidUUID = "not-a-uuid";

        // When / Then
        mockMvc.perform(get("/tasks/{id}", invalidUUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}