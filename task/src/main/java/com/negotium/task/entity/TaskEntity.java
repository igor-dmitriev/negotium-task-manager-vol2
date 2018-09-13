package com.negotium.task.entity;

import com.negotium.common.meta.TaskStatus;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class TaskEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasks_seq")
  @SequenceGenerator(name = "tasks_seq", sequenceName = "tasks_seq", allocationSize = 20)
  private Long id;

  private String description;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Enumerated(EnumType.STRING)
  @Column
  private TaskStatus status = TaskStatus.ACTIVE;

  public boolean isActive() {
    return status == TaskStatus.ACTIVE;
  }
}
