package com.lseg.todolist.task.application.deletetask;

import com.lseg.todolist.task.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskDeleter {
    private final TaskRepository taskRepository;

    public TaskDeleter(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void executeDeleteTask(UUID id) {
        if (taskRepository.getTaskById(id).isEmpty()) {
            throw new IllegalArgumentException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}