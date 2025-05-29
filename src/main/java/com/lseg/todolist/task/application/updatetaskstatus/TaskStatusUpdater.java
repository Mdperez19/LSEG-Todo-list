package com.lseg.todolist.task.application.updatetaskstatus;

import com.lseg.todolist.task.domain.entity.Task;
import com.lseg.todolist.task.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskStatusUpdater {
    private final TaskRepository taskRepository;

    public TaskStatusUpdater(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task executeUpdateTaskStatus(UUID id, UpdateTaskStatusCommand command) {

        Task task = taskRepository.getTaskById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));

        task.setStatus(command.status());

        return taskRepository.save(task);
    }
}