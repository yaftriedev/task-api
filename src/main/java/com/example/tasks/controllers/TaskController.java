package com.example.tasks.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tasks.config.JwtManager;
import com.example.tasks.dto.TaskCreatedDTO;
import com.example.tasks.dto.TaskDTO;
import com.example.tasks.services.TaskService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/task")
public class TaskController {
    
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(
            taskService.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getMethodName(@PathVariable Long id) {
        return ResponseEntity.ok(
            taskService.findById(id)
        );
    }
    
    @PostMapping
    public ResponseEntity<TaskDTO> addTask(
        @RequestBody TaskCreatedDTO taskCreatedDTO,
        HttpServletRequest request
    ) {
        
        Long userId = JwtManager.getIdFromToken(
            JwtManager.getTokenFromHeader(request.getHeader("Authorization"))
        );
        
        return ResponseEntity.ok(
            taskService.createTask(taskCreatedDTO.getDescription(), userId)
        );
    }

    @PutMapping("/{id}/description")
    public ResponseEntity<TaskDTO> changeDescription(
        @PathVariable Long id, 
        @RequestBody TaskCreatedDTO taskCreatedDTO
    ) {
        return ResponseEntity.ok(
            taskService.changeDescription(id, taskCreatedDTO.getDescription())
        );
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDTO> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(
            taskService.deleteById(id)
        );
    }

}
