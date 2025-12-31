package com.qadri.tada.repository;

import com.qadri.tada.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByLastUpdateTimeGreaterThanAndUser_Id(Instant lastSyncTime, Long userId);

    List<TaskEntity> findByUser_Id(Long userId);
}