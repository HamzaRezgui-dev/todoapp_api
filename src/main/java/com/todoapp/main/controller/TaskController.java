package com.todoapp.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todoapp.main.dto.TaskDto;
import com.todoapp.main.dto.TaskResponseDto;
import com.todoapp.main.service.TaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private PagedResourcesAssembler<TaskResponseDto> assembler;

    @PostMapping("/todos")
    public ResponseEntity<?> addTask(@RequestBody TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> putTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }

    @GetMapping("/todos")
    public HttpEntity<PagedModel<EntityModel<TaskResponseDto>>> getTasks(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false, name = "sort") String sortBy,
            @RequestParam(required = false, defaultValue = "asc", name = "direction") String sortDirection) {
        Page<TaskResponseDto> tasksPage = taskService.getTasks(page, size, sortBy, sortDirection);

        PagedModel<EntityModel<TaskResponseDto>> pagedModel = assembler.toModel(tasksPage);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/filter")
    public HttpEntity<PagedModel<EntityModel<TaskResponseDto>>> filterTasks(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false, name = "filter") String descriptionFilter) {
        Page<TaskResponseDto> filteredTasks = taskService.filterTasks(page, size, descriptionFilter);

        PagedModel<EntityModel<TaskResponseDto>> pagedModel = assembler.toModel(filteredTasks);
        return ResponseEntity.ok(pagedModel);
    }

}