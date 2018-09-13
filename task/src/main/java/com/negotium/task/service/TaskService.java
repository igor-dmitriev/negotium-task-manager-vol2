package com.negotium.task.service;

import com.negotium.common.meta.TaskStatus;
import com.negotium.task.entity.TaskEntity;

import java.util.List;

public interface TaskService {
  TaskEntity save(TaskEntity task);

  List<TaskEntity> getAll(TaskStatus status);

  void completeTask(Long id);

  TaskEntity get(Long id);
}
