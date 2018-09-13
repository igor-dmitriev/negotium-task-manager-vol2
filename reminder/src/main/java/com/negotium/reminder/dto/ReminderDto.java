package com.negotium.reminder.dto;

import com.negotium.reminder.meta.ReminderStatus;
import com.negotium.reminder.meta.ReminderType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReminderDto {
  private Long taskId;
  private String remindAt;
  private ReminderType type;
  private ReminderStatus status;
}
