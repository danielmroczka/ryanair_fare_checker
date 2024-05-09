package com.dm.labs.ryanairwebscrapper.controller;

import com.dm.labs.ryanairwebscrapper.entity.Task;
import com.dm.labs.ryanairwebscrapper.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> readAllTask() {
        return service.readAllTask();
    }

    @PostMapping
    public void createTask(@RequestBody @Valid Task task) {
        service.addNewTask(task);
    }

}
