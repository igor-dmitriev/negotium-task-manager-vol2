package com.negotium.task.dto;

import com.negotium.common.meta.TaskStatus;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDto {
  private Long id;
  private String description;
  private LocalDateTime createdAt;
  private TaskStatus status;
}
