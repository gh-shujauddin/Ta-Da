package com.qadri.tada.repository;

import com.qadri.tada.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByLastUpdateTimeGreaterThanAndUser_Id(Instant lastSyncTime, Long userId);

    List<TaskEntity> findByUser_Id(Long userId);

    Optional<TaskEntity> findByIdAndUser_Id(Long id, Long userId);
}