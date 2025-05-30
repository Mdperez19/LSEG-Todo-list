package com.lseg.todolist.task.application.createtask;

import com.lseg.todolist.task.domain.entity.Status;
import com.lseg.todolist.task.domain.entity.Task;
import com.lseg.todolist.task.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreationTaskTest {

    @Mock
    private TaskRepository taskRepository;

    private CreationTask creationTask;

    @BeforeEach
    void setUp() {
        creationTask = new CreationTask(taskRepository);
    }

    @Test
    @DisplayName("Should create a task with PENDING status")
    void shouldCreateTaskWithPendingStatus() {
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

        when(taskRepository.save(any(Task.class))).thenReturn(expectedTask);

        // When
        Task result = creationTask.executeCreationTask(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(Status.PENDING);
        assertThat(result.getTitle()).isEqualTo(command.title());
        assertThat(result.getDescription()).isEqualTo(command.description());
        assertThat(result.getDueDate()).isEqualTo(command.dueDate());

        verify(taskRepository).save(any(Task.class));
    }
}