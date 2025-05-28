package com.lseg.todolist.task.domain.repository;

import com.lseg.todolist.task.domain.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskRepository {
    Task createTask(Task task);
    Page<Task> getAllTasks(Pageable pageable);
}
