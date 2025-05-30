package com.lseg.todolist.task.application.readalltasks;

import com.lseg.todolist.task.domain.entity.Task;
import com.lseg.todolist.task.domain.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class TaskReader {
    private final TaskRepository taskRepository;

    public TaskReader(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Page<Task> executeTaskReader(Pageable pageable) {
        log.info("Executing task reader with pageable: {}", pageable);

        Page<Task> tasksPage = taskRepository.getAllTasks(pageable);
        log.info("Task reader returned {} tasks", tasksPage.getTotalElements());
        return tasksPage;
    }
}
