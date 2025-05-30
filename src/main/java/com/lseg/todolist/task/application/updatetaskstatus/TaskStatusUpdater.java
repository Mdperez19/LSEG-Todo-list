package com.lseg.todolist.task.application.updatetaskstatus;

import com.lseg.todolist.task.domain.entity.Task;
import com.lseg.todolist.task.domain.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class TaskStatusUpdater {
    private final TaskRepository taskRepository;

    public TaskStatusUpdater(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task executeUpdateTaskStatus(UUID id, UpdateTaskStatusCommand command) {

        log.info("Executing task status update for task ID: {} with command: {}", id, command);
        Task task = taskRepository.getTaskById(id)
                .orElseThrow(() -> {
                    log.error("Task with ID: {} not found", id);
                    return new IllegalArgumentException("Task not found with id: " + id);
                });

        task.setStatus(command.status());
        log.info("Updated task status to: {}", command.status());

        return taskRepository.save(task);
    }
}