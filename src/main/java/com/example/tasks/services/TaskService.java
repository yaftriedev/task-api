package com.example.tasks.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.tasks.dto.TaskDTO;
import com.example.tasks.model.Task;
import com.example.tasks.model.User;
import com.example.tasks.repository.TaskRepository;
import com.example.tasks.repository.UserRepository;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(
        UserRepository userRepository,
        TaskRepository taskRepository
    ){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskDTO> findAll() {
        return taskRepository.findAll()
            .stream()
            .map(TaskDTO::fromEntity)
            .toList();
    }

    public TaskDTO findById(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow( () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Error, task not found"
            ));

        return TaskDTO.fromEntity(task);
    }

    public TaskDTO createTask(String Description, Long userId) {

        String description = Optional.ofNullable(Description)
            .filter(d -> !d.isBlank())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Error, you need to specify a Description"
            ));

        User createdUser = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Error, this id isn't correct"
            ));

        Task newTask = new Task();
        newTask.setDescription(description);
        newTask.setCreatedUser(createdUser);
        
        taskRepository.save(newTask);
        return TaskDTO.fromEntity(newTask);
        
    }

    public TaskDTO changeDescription(Long id, String newDescription) {
        
        if (newDescription.equals("") || newDescription.equals(null)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "The description musn't be empty"
            );
        }

        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Error, the task don't exist"
            ));
        
        task.setDescription(newDescription);
        taskRepository.save(task);

        return TaskDTO.fromEntity(task);
    }

    public TaskDTO deleteById(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow( () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Error, the task that you want to delete not exist"
            ));

        taskRepository.delete(task);
        return TaskDTO.fromEntity(task);
    }

}
