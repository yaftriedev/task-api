package com.example.tasks.dto;

import com.example.tasks.model.*;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskDTO {
    Long id;
    String Task;
    String createdUserName;

    public TaskDTO() {}

    public TaskDTO(Long id, String Task, String createdUserName) {
        this.id = id;
        this.Task = Task;
        this.createdUserName = createdUserName;
    }

    public static TaskDTO fromEntity(Task task) {
        return new TaskDTO(
            task.getId(),
            task.getDescription(),
            task.getCreatedUser() != null ? task.getCreatedUser().getName() : ""
        );
    }
}
