package com.lseg.todolist.task.infrastructure.controller;

import com.lseg.todolist.task.application.createtask.CreationTask;
import com.lseg.todolist.task.application.createtask.CreationTaskCommand;
import com.lseg.todolist.task.application.readalltasks.TaskReader;
import com.lseg.todolist.task.domain.entity.Task;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/tasks")
public class TaskRestController {

    private final CreationTask creationTask;
    private final TaskReader taskReader;

    public TaskRestController(CreationTask creationTask, TaskReader taskReader) {
        this.creationTask = creationTask;
        this.taskReader = taskReader;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody CreationTaskCommand task) {
        return creationTask.executeCreationTask(task);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Task> getTasks(@ParameterObject Pageable pageable) {
        return taskReader.executeTaskReader(pageable);
    }
}
