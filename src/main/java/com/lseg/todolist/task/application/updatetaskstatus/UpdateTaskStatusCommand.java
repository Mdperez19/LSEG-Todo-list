package com.lseg.todolist.task.application.updatetaskstatus;

import com.lseg.todolist.task.domain.entity.Status;

public record UpdateTaskStatusCommand(
        Status status
) {
}

