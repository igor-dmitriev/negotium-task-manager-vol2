package com.negotium.task.repository;

import com.negotium.common.meta.TaskStatus;
import com.negotium.task.entity.TaskEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
  List<TaskEntity> findByStatusOrderByCreatedAt(TaskStatus status);

  List<TaskEntity> findAllByOrderByCreatedAt();

  List<TaskEntity> findByStatus(TaskStatus status);
}
