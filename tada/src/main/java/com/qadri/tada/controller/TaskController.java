package com.qadri.tada.controller;

import com.qadri.tada.dto.TaskDto;
import com.qadri.tada.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @GetMapping("all")
    public ResponseEntity<List<TaskDto>> getTaskSince(@RequestParam(required = false) Long lastSyncTime) {
        return ResponseEntity.ok(taskService.getAllTasks(lastSyncTime));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

//    @PostMapping
//    public ResponseEntity<TaskDto> addTask(@RequestBody TaskDto taskDto) {
//        TaskDto task = taskService.createTask(taskDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(task);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> upsertTask(@PathVariable("id") Long id, @RequestBody TaskDto taskDto) {
        System.out.println("Request task dto: " + taskDto);
        TaskDto task = taskService.upsertTask(id, taskDto);
        log.debug("TASK: {}", task);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}
