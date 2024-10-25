package com.todoapp.main.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.todoapp.main.dto.TaskDto;
import com.todoapp.main.dto.TaskResponseDto;
import com.todoapp.main.entity.Task;
import com.todoapp.main.entity.User;
import com.todoapp.main.repository.TaskRepository;
import com.todoapp.main.repository.UserRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> createTask(TaskDto taskDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            User user = userRepository.findByEmail(currentUserEmail);

            Task task = new Task();
            task.setTitle(taskDto.getTitle());
            task.setDescription(taskDto.getDescription());
            task.setUser(user);

            taskRepository.save(task);

            TaskResponseDto taskResponse = new TaskResponseDto(task.getId(), task.getTitle(), task.getDescription());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(taskResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while creating the task: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> updateTask(Long id, TaskDto taskDto) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            Task task = taskRepository.findById(id).orElse(null);
            if (task == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Task not found"));
            }

            if (!task.getUser().getEmail().equals(currentUserEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Forbidden"));
            }

            task.setTitle(taskDto.getTitle());
            task.setDescription(taskDto.getDescription());

            taskRepository.save(task);

            TaskResponseDto taskResponse = new TaskResponseDto(task.getId(), task.getTitle(), task.getDescription());
            return ResponseEntity.ok(taskResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while updating the task: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> deleteTask(Long id) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            Task task = taskRepository.findById(id).orElse(null);
            if (task == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Task not found"));
            }

            if (!task.getUser().getEmail().equals(currentUserEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Forbidden"));
            }

            taskRepository.delete(task);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while deleting the task: " + e.getMessage()));
        }
    }

    public Page<TaskResponseDto> getTasks(int page, int size, String sortBy, String sortDirection) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        User user = userRepository.findByEmail(currentUserEmail);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + currentUserEmail);
        }

        Pageable pageable;

        if (sortBy != null && !sortBy.isEmpty()) {
            Sort sort;
            if ("desc".equalsIgnoreCase(sortDirection)) {
                sort = Sort.by(sortBy).descending();
            } else {
                sort = Sort.by(sortBy).ascending();
            }
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size);
        }

        return taskRepository.findByUser(pageable, user).map(task -> new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription()));
    }

    public Page<TaskResponseDto> filterTasks(int page, int size, String filter) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        User user = userRepository.findByEmail(currentUserEmail);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + currentUserEmail);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Task> tasksPage;
        if (filter != null && !filter.isEmpty()) {
            tasksPage = taskRepository.findByUserAndDescriptionOrTitleContaining(pageable, user, filter, filter);
        } else {
            tasksPage = taskRepository.findByUser(pageable, user);
        }

        return tasksPage.map(task -> new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription()));
    }

}
