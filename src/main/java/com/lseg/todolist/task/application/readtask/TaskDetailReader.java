package com.lseg.todolist.task.application.readtask;

import com.lseg.todolist.task.domain.entity.Task;
import com.lseg.todolist.task.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TaskDetailReader {
    private final TaskRepository taskRepository;

    public TaskDetailReader(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Optional<Task> executeTaskDetailReader(UUID id) {
        return taskRepository.getTaskById(id);
    }
}
