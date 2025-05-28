package com.lseg.todolist.task.infrastructure.controller;

import com.lseg.todolist.task.application.createtask.CreationTask;
import com.lseg.todolist.task.application.createtask.CreationTaskCommand;
import com.lseg.todolist.task.domain.entity.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/tasks")
public class TaskRestController {

    private final CreationTask creationTask;

    public TaskRestController(CreationTask creationTask) {
        this.creationTask = creationTask;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody CreationTaskCommand task) {
        return creationTask.executeCreationTask(task);
    }
}
