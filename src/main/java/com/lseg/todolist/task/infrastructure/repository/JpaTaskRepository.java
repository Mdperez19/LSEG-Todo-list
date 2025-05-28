package com.lseg.todolist.task.infrastructure.repository;

import com.lseg.todolist.task.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaTaskRepository extends JpaRepository<Task, UUID> {
}
