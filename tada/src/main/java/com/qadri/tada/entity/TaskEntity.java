package com.qadri.tada.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "tasks")
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

    @Column(nullable = false)
    private Boolean isCompleted;

    @Column(nullable = false)
    private Boolean isDeleted;

    @UpdateTimestamp
    private Instant lastUpdateTime;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
