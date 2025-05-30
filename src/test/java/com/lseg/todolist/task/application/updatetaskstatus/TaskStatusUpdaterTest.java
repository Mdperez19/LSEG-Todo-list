package com.lseg.todolist.task.application.updatetaskstatus;

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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskStatusUpdaterTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskStatusUpdater taskStatusUpdater;
    private UUID taskId;
    private Task existingTask;

    @BeforeEach
    void setUp() {
        taskStatusUpdater = new TaskStatusUpdater(taskRepository);
        taskId = UUID.randomUUID();
        existingTask = Task.builder()
                .id(taskId)
                .title("Test Task")
                .description("Test Description")
                .dueDate(LocalDate.now())
                .status(Status.PENDING)
                .build();
    }

    @Test
    @DisplayName("Should update task status when task exists")
    void shouldUpdateTaskStatusWhenTaskExists() {
        // Given
        var command = new UpdateTaskStatusCommand(Status.IN_PROGRESS);
        when(taskRepository.getTaskById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        // When
        Task updatedTask = taskStatusUpdater.executeUpdateTaskStatus(taskId, command);

        // Then
        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getStatus()).isEqualTo(Status.IN_PROGRESS);
        verify(taskRepository).getTaskById(taskId);
        verify(taskRepository).save(existingTask);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when task not found")
    void shouldThrowExceptionWhenTaskNotFound() {
        // Given
        var command = new UpdateTaskStatusCommand(Status.IN_PROGRESS);
        when(taskRepository.getTaskById(taskId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> taskStatusUpdater.executeUpdateTaskStatus(taskId, command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Task not found with id: " + taskId);

        verify(taskRepository).getTaskById(taskId);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should maintain other task properties when updating status")
    void shouldMaintainOtherPropertiesWhenUpdatingStatus() {
        // Given
        var command = new UpdateTaskStatusCommand(Status.COMPLETED);
        when(taskRepository.getTaskById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        // When
        Task updatedTask = taskStatusUpdater.executeUpdateTaskStatus(taskId, command);

        // Then
        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getId()).isEqualTo(existingTask.getId());
        assertThat(updatedTask.getTitle()).isEqualTo(existingTask.getTitle());
        assertThat(updatedTask.getDescription()).isEqualTo(existingTask.getDescription());
        assertThat(updatedTask.getDueDate()).isEqualTo(existingTask.getDueDate());
        assertThat(updatedTask.getStatus()).isEqualTo(Status.COMPLETED);
    }
}