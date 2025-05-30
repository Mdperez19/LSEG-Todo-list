package com.lseg.todolist.task.application.deletetask;

import com.lseg.todolist.task.domain.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class TaskDeleter {
    private final TaskRepository taskRepository;

    public TaskDeleter(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void executeDeleteTask(UUID id) {
        log.info("Executing task deletion for task ID: {}", id);
        if (taskRepository.getTaskById(id).isEmpty()) {
            throw new IllegalArgumentException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
        log.info("Successfully deleted task with ID: {}", id);
    }
}