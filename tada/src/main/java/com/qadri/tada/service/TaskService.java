package com.qadri.tada.service;

import com.qadri.tada.dto.TaskDto;
import com.qadri.tada.entity.TaskEntity;
import com.qadri.tada.entity.User;
import com.qadri.tada.error.IdNotMatchedException;
import com.qadri.tada.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class TaskService {

    private final ModelMapper modelMapper;

    private final TaskRepository taskRepository;

    public List<TaskDto> getAllTasks(Long lastSyncTime) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TaskEntity> entities;
        if (lastSyncTime == null) {
            entities = taskRepository.findByUser_Id(user.getId());
        } else {
            Instant syncTime = Instant.ofEpochMilli(lastSyncTime);
            entities = taskRepository.findByLastUpdateTimeGreaterThanAndUser_Id(syncTime, user.getId());
        }
        return entities.stream()
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaskEntity taskEntity = taskRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        return modelMapper.map(taskEntity, TaskDto.class);
    }

    public void deleteTaskById(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        TaskEntity task = taskRepository.findByIdAndUser_Id(id, user.getId()).orElseThrow(() ->
                new EntityNotFoundException("Cannot delete. Task not found with id: " + id));
        task.setIsDeleted(true);
        taskRepository.save(task);
    }

    public TaskDto upsertTask(Long id, TaskDto taskDto) {
        TaskEntity entity = taskRepository.findById(id).orElseGet(() -> {
                    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    return TaskEntity.builder()
                            .id(id)
                            .user(user)
                            .build();
                }
        );
        //TODO handle id mismatch scenarios

        entity.setTaskName(taskDto.getTaskName());
        entity.setTaskDescription(taskDto.getTaskDescription());
        entity.setIsCompleted(taskDto.getIsCompleted());
        entity.setIsDeleted(taskDto.getIsDeleted());

        entity = taskRepository.save(entity);

        log.debug("Entity upsert: {}", entity);
        return modelMapper.map(entity, TaskDto.class);
    }
}
