package com.lseg.todolist.task.application.readalltasks;

import com.lseg.todolist.task.domain.entity.Task;
import com.lseg.todolist.task.domain.entity.Status;
import com.lseg.todolist.task.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskReaderTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskReader taskReader;

    @BeforeEach
    void setUp() {
        taskReader = new TaskReader(taskRepository);
    }

    @Test
    @DisplayName("Should get page of tasks when requesting task list")
    void should_get_page_of_tasks_when_requesting_task_list() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Task> taskList = List.of(
                Task.builder()
                        .id(UUID.randomUUID())
                        .title("Complete unit tests")
                        .description("Implement unit tests for TaskReader")
                        .dueDate(LocalDate.now().plusDays(1))
                        .status(Status.PENDING)
                        .build(),
                Task.builder()
                        .id(UUID.randomUUID())
                        .title("Review pull request")
                        .description("Review and approve pending PR")
                        .dueDate(LocalDate.now().plusDays(2))
                        .status(Status.IN_PROGRESS)
                        .build()
        );
        Page<Task> expectedPage = new PageImpl<>(taskList, pageable, taskList.size());
        when(taskRepository.getAllTasks(pageable)).thenReturn(expectedPage);

        // When
        Page<Task> result = taskReader.executeTaskReader(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).isEqualTo(taskList);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(taskRepository).getAllTasks(pageable);
    }

    @Test
    @DisplayName("Should return empty page when no tasks exist")
    void should_return_empty_page_when_no_tasks_exist() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(taskRepository.getAllTasks(pageable)).thenReturn(emptyPage);

        // When
        Page<Task> result = taskReader.executeTaskReader(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
        verify(taskRepository).getAllTasks(pageable);
    }
}