package com.qadri.tada.service;

import com.qadri.tada.dto.TaskDto;
import com.qadri.tada.entity.TaskEntity;
import com.qadri.tada.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final ModelMapper modelMapper;

    private final TaskRepository taskRepository;

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .toList();
    }

//    public TaskDto createTask(TaskDto taskDto) {
//        if (taskRepository.existsById(taskDto.getId())) {
//            throw new IllegalArgumentException("Task already exist with task id- " + taskDto.getId());
//        }
//
//        TaskEntity task = modelMapper.map(taskDto, TaskEntity.class);
//        TaskEntity savedTask = taskRepository.save(task);
//
//        return modelMapper.map(savedTask, TaskDto.class);
//    }

    public TaskDto getTaskById(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        return modelMapper.map(taskEntity, TaskDto.class);
    }

    public void deleteTaskById(Long id) {
        TaskEntity task = taskRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Cannot delete. Task not found with id: " + id));
        task.setDeleted(true);
        task.setLastUpdateTime(System.currentTimeMillis());
        taskRepository.save(task);
    }

    public TaskDto upsertTask(Long id, TaskDto taskDto) {
        TaskEntity entity = taskRepository.findById(id).orElseGet(() ->
                TaskEntity.builder()
                        .id(id)
                        .createdAt(System.currentTimeMillis())
                        .build()
        );

        entity.setTaskName(taskDto.getTaskName());
        entity.setTaskDescription(taskDto.getTaskDescription());
        entity.setCompleted(entity.isCompleted());

        entity.setLastUpdateTime(System.currentTimeMillis());

        return modelMapper.map(taskRepository.save(entity), TaskDto.class);
    }
}
