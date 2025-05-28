package com.lseg.todolist.task.infrastructure.repository;

import com.lseg.todolist.task.domain.entity.Task;
import com.lseg.todolist.task.domain.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public class PostgresTaskRepository implements TaskRepository {
    private final JpaTaskRepository jpaTaskRepository;

    public PostgresTaskRepository(JpaTaskRepository jpaTaskRepository) {
        this.jpaTaskRepository = jpaTaskRepository;
    }

    @Override
    public Task createTask(Task task) {
        return jpaTaskRepository.save(task);
    }

    @Override
    public Page<Task> getAllTasks(Pageable pageable) {
        return jpaTaskRepository.findAll(pageable);
    }
}
