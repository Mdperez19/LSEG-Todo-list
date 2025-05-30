package com.lseg.todolist.task.infrastructure.controller;

import com.lseg.todolist.task.application.createtask.CreationTask;
import com.lseg.todolist.task.application.createtask.CreationTaskCommand;
import com.lseg.todolist.task.application.deletetask.TaskDeleter;
import com.lseg.todolist.task.application.readalltasks.TaskReader;
import com.lseg.todolist.task.application.readtask.TaskDetailReader;
import com.lseg.todolist.task.application.updatetaskstatus.TaskStatusUpdater;
import com.lseg.todolist.task.application.updatetaskstatus.UpdateTaskStatusCommand;
import com.lseg.todolist.task.domain.entity.Task;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TaskRestController {

    private final CreationTask creationTask;
    private final TaskReader taskReader;
    private final TaskDetailReader taskDetailReader;
    private final TaskStatusUpdater taskStatusUpdater;
    private final TaskDeleter taskDeleter;

    public TaskRestController(CreationTask creationTask, TaskReader taskReader, TaskDetailReader taskDetailReader, TaskStatusUpdater taskStatusUpdater, TaskDeleter taskDeleter) {
        this.creationTask = creationTask;
        this.taskReader = taskReader;
        this.taskDetailReader = taskDetailReader;
        this.taskStatusUpdater = taskStatusUpdater;
        this.taskDeleter = taskDeleter;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody CreationTaskCommand task) {
        log.info("Create task in TaskRestController: {}", task);
        return creationTask.executeCreationTask(task);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Task> getTasks(@ParameterObject Pageable pageable) {
        log.info("Fetching tasks with pageable in TaskRestController: {}", pageable);
        return taskReader.executeTaskReader(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable UUID id) {
        log.info("Fetching task by ID in TaskRestController: {}", id);
        return taskDetailReader.executeTaskDetailReader(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable UUID id,
            @RequestBody UpdateTaskStatusCommand command) {
        log.info("Updating task status in TaskRestController for ID: {} with command: {}", id, command);
        try {
            Task updatedTask = taskStatusUpdater.executeUpdateTaskStatus(id, command);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException e) {
            log.error("Task not found for ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        log.info("Deleting task in TaskRestController for ID: {}", id);
        try {
            taskDeleter.executeDeleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Task not found for ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

}
