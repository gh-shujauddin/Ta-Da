package com.qadri.tada.service;

import com.qadri.tada.model.TaskResponseDto;
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

    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(task -> modelMapper.map(task, TaskResponseDto.class))
                .toList();
    }
}
