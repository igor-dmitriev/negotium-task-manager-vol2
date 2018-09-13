package com.negotium.task.mapper;

import com.negotium.task.dto.TaskDto;
import com.negotium.task.entity.TaskEntity;

public class TaskMapper {
  private TaskMapper() {

  }

  public static TaskDto map(TaskEntity entity) {
    if (entity == null) {
      return null;
    }
    TaskDto dto = new TaskDto();
    dto.setId(entity.getId());
    dto.setStatus(entity.getStatus());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setDescription(entity.getDescription());
    return dto;
  }

  public static TaskEntity map(TaskDto dto) {
    if (dto == null) {
      return null;
    }
    TaskEntity entity = new TaskEntity();
    if (dto.getStatus() != null) {
      entity.setStatus(dto.getStatus());
    }
    entity.setCreatedAt(dto.getCreatedAt());
    entity.setDescription(dto.getDescription());
    return entity;
  }
}
