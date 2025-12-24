package com.qadri.tada.service;

import com.qadri.tada.model.TaskDto;
import com.qadri.tada.model.TaskEntity;
import com.qadri.tada.repository.TaskRepository;
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

    public TaskDto createTask(TaskDto taskDto) {
        if (taskRepository.existsById(taskDto.getId())) {
            throw new IllegalArgumentException("Task already exist with task id- " + taskDto.getId());
        }

        TaskEntity task = modelMapper.map(taskDto, TaskEntity.class);
        TaskEntity savedTask = taskRepository.save(task);

        return modelMapper.map(savedTask, TaskDto.class);
    }

    public TaskDto getTaskById(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
        return modelMapper.map(taskEntity, TaskDto.class);
    }

    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete. Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    public TaskDto updateTask(Long id, TaskDto taskDto) {
        boolean taskExits = taskRepository.existsById(id);
        if (!taskExits) {
            throw new IllegalArgumentException("Task not found with task id: " + taskDto.getId());
        }
        TaskEntity taskEntity = modelMapper.map(taskDto, TaskEntity.class);
        TaskEntity savedTask = taskRepository.save(taskEntity);

        return modelMapper.map(savedTask, TaskDto.class);
    }
}
