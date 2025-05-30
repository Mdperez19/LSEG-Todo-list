package com.lseg.todolist.task.application.createtask;

import com.lseg.todolist.task.domain.entity.Status;
import com.lseg.todolist.task.domain.entity.Task;
import com.lseg.todolist.task.domain.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreationTask {

    private final TaskRepository taskRepository;

    public CreationTask(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task executeCreationTask(CreationTaskCommand creationTaskCommand) {
        log.info("Executing task creation with command: {}", creationTaskCommand);
        Task newTask = Task.builder()
                .title(creationTaskCommand.title())
                .description(creationTaskCommand.description())
                .dueDate(creationTaskCommand.dueDate())
                .status(Status.PENDING)
                .build();

        Task savedTask = taskRepository.save(newTask);
        log.info("Successfully created task with ID: {}", savedTask.getId());

        return savedTask;

    }
}
