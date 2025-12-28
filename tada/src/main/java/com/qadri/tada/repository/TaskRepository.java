package com.qadri.tada.repository;

import com.qadri.tada.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByLastUpdateTimeGreaterThan(Long lastSyncTime);
}