package com.lseg.todolist.task.application.createtask;

import com.lseg.todolist.task.domain.entity.Status;
import com.lseg.todolist.task.domain.entity.Task;
import com.lseg.todolist.task.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class CreationTask {

    private final TaskRepository taskRepository;

    public CreationTask(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task executeCreationTask(CreationTaskCommand creationTaskCommand) {
        Task newTask = Task.builder()
                .title(creationTaskCommand.title())
                .description(creationTaskCommand.description())
                .dueDate(creationTaskCommand.dueDate())
                .status(Status.PENDING)
                .build();

        return taskRepository.createTask(newTask);
    }
}
