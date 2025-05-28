package com.lseg.todolist.task.application.readalltasks;

import com.lseg.todolist.task.domain.entity.Task;
import com.lseg.todolist.task.domain.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class TaskReader {
    private final TaskRepository taskRepository;

    public TaskReader(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Page<Task> executeTaskReader(Pageable pageable) {
        return taskRepository.getAllTasks(pageable);
    }
}
