package com.lseg.todolist.task.infrastructure.controller;

import com.lseg.todolist.task.application.createtask.CreationTask;
import com.lseg.todolist.task.application.createtask.CreationTaskCommand;
import com.lseg.todolist.task.application.readalltasks.TaskReader;
import com.lseg.todolist.task.application.readtask.TaskDetailReader;
import com.lseg.todolist.task.application.updatetaskstatus.TaskStatusUpdater;
import com.lseg.todolist.task.application.updatetaskstatus.UpdateTaskStatusCommand;
import com.lseg.todolist.task.domain.entity.Task;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/tasks")
public class TaskRestController {

    private final CreationTask creationTask;
    private final TaskReader taskReader;
    private final TaskDetailReader taskDetailReader;
    private final TaskStatusUpdater taskStatusUpdater;

    public TaskRestController(CreationTask creationTask, TaskReader taskReader, TaskDetailReader taskDetailReader, TaskStatusUpdater taskStatusUpdater) {
        this.creationTask = creationTask;
        this.taskReader = taskReader;
        this.taskDetailReader = taskDetailReader;
        this.taskStatusUpdater = taskStatusUpdater;
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

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable UUID id) {
        return taskDetailReader.executeTaskDetailReader(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable UUID id,
            @RequestBody UpdateTaskStatusCommand command) {
        try {
            Task updatedTask = taskStatusUpdater.executeUpdateTaskStatus(id, command);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
