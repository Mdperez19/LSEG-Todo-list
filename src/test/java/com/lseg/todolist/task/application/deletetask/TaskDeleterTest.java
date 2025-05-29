package com.lseg.todolist.task.application.deletetask;

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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskDeleterTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskDeleter taskDeleter;
    private UUID taskId;
    private Task existingTask;

    @BeforeEach
    void setUp() {
        taskDeleter = new TaskDeleter(taskRepository);
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
    @DisplayName("Should delete task when task exists")
    void shouldDeleteTaskWhenTaskExists() {
        // Given
        when(taskRepository.getTaskById(taskId)).thenReturn(Optional.of(existingTask));

        // When
        taskDeleter.executeDeleteTask(taskId);

        // Then
        verify(taskRepository).getTaskById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when task not found")
    void shouldThrowExceptionWhenTaskNotFound() {
        // Given
        when(taskRepository.getTaskById(taskId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> taskDeleter.executeDeleteTask(taskId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Task not found with id: " + taskId);

        verify(taskRepository).getTaskById(taskId);
        verify(taskRepository, never()).deleteById(any());
    }
}