package com.qadri.tada.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDto {
    private Long id;
    private String taskName;
    private String taskDescription;
    private Boolean isCompleted;
    private Boolean isDeleted;
    private Instant lastUpdateTime;
    private Instant createdAt;
}
