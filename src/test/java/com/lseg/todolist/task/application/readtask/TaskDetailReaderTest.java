package com.lseg.todolist.task.application.readtask;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskDetailReaderTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskDetailReader taskDetailReader;

    @BeforeEach
    void setUp() {
        taskDetailReader = new TaskDetailReader(taskRepository);
    }

    @Test
    @DisplayName("Should return task when task exists with given id")
    void should_return_task_when_task_exists_with_given_id() {
        // Given
        UUID taskId = UUID.randomUUID();
        Task expectedTask = Task.builder()
                .id(taskId)
                .title("Complete unit test")
                .description("Write BDD style test for TaskDetailReader")
                .dueDate(LocalDate.now().plusDays(1))
                .status(Status.PENDING)
                .build();
        when(taskRepository.getTaskById(taskId)).thenReturn(Optional.of(expectedTask));

        // When
        Optional<Task> result = taskDetailReader.executeTaskDetailReader(taskId);

        // Then
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(task -> {
                    assertThat(task.getId()).isEqualTo(taskId);
                    assertThat(task.getTitle()).isEqualTo("Complete unit test");
                    assertThat(task.getDescription()).isEqualTo("Write BDD style test for TaskDetailReader");
                    assertThat(task.getStatus()).isEqualTo(Status.PENDING);
                });
        verify(taskRepository).getTaskById(taskId);
    }

    @Test
    @DisplayName("Should return empty optional when task does not exist")
    void should_return_empty_optional_when_task_does_not_exist() {
        // Given
        UUID nonExistentTaskId = UUID.randomUUID();
        when(taskRepository.getTaskById(nonExistentTaskId)).thenReturn(Optional.empty());

        // When
        Optional<Task> result = taskDetailReader.executeTaskDetailReader(nonExistentTaskId);

        // Then
        assertThat(result).isEmpty();
        verify(taskRepository).getTaskById(nonExistentTaskId);
    }
}