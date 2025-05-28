package com.lseg.todolist.task.domain.repository;

import com.lseg.todolist.task.domain.entity.Task;

public interface TaskRepository {
    Task createTask(Task task);
}
