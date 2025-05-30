package com.lseg.todolist.task.application.readtask;

import com.lseg.todolist.task.domain.entity.Task;
import com.lseg.todolist.task.domain.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class TaskDetailReader {
    private final TaskRepository taskRepository;

    public TaskDetailReader(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Optional<Task> executeTaskDetailReader(UUID id) {
        log.info("Executing task detail reader for task ID: {}", id);

        Optional<Task> returnedTask = taskRepository.getTaskById(id);

        log.info("Task detail reader returned: {}", returnedTask);

        return returnedTask;
    }
}
