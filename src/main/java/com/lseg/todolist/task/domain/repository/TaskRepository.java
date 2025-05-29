package com.lseg.todolist.task.domain.repository;

import com.lseg.todolist.task.domain.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    Task save(Task task);
    Page<Task> getAllTasks(Pageable pageable);
    Optional<Task> getTaskById(UUID id);
    void deleteById(UUID id);
}
