package com.qadri.tada.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "tasks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    private String taskName;

    private String taskDescription;
    private Boolean isCompleted;
    private Boolean isDeleted;
    private Long lastUpdateTime;
    private Long createdAt;
}
