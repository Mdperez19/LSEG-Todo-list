package com.lseg.todolist.task.application.createtask;

import java.time.LocalDate;

public record CreationTaskCommand(
    String title,
    String description,
    LocalDate dueDate
) {
}
