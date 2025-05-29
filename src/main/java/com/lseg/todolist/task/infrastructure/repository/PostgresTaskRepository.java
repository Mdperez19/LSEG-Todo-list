package com.lseg.todolist.task.infrastructure.repository;

import com.lseg.todolist.task.domain.entity.Task;
import com.lseg.todolist.task.domain.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public class PostgresTaskRepository implements TaskRepository {
    private final JpaTaskRepository jpaTaskRepository;

    public PostgresTaskRepository(JpaTaskRepository jpaTaskRepository) {
        this.jpaTaskRepository = jpaTaskRepository;
    }

    @Override
    public Task save(Task task) {
        return jpaTaskRepository.save(task);
    }

    @Override
    public Page<Task> getAllTasks(Pageable pageable) {
        return jpaTaskRepository.findAll(pageable);
    }

    @Override
    public Optional<Task> getTaskById(UUID id) {
        return jpaTaskRepository.findById(id);
    }

}
