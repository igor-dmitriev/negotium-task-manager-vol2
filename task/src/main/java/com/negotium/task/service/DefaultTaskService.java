package com.negotium.task.service;

import com.negotium.common.error.ResourceNotFoundException;
import com.negotium.common.meta.TaskStatus;
import com.negotium.task.entity.TaskEntity;
import com.negotium.task.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultTaskService implements TaskService {
  private final TaskRepository taskRepository;

  @Override
  public List<TaskEntity> getAll(TaskStatus status) {
    if (status != null) {
      return taskRepository.findByStatusOrderByCreatedAt(status);
    }
    return taskRepository.findAllByOrderByCreatedAt();
  }

  @Override
  @Transactional
  public TaskEntity save(TaskEntity task) {
    return taskRepository.save(task);
  }

  @Override
  @Transactional
  public void completeTask(Long id) {
    TaskEntity task = findByIdOrElseThrowNotFound(id);
    task.setStatus(TaskStatus.COMPLETED);
  }

  @Override
  public TaskEntity get(Long id) {
    return findByIdOrElseThrowNotFound(id);
  }

  private TaskEntity findByIdOrElseThrowNotFound(Long id) {
    return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("TaskEntity not found with id:" + id));
  }

}

