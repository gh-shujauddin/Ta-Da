package com.qadri.tada.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Long id;
    private String taskName;
    private String taskDescription;
    private boolean isCompleted;
    private boolean isDeleted;
    private Long lastUpdateTime;
    private Long createdAt;
}
