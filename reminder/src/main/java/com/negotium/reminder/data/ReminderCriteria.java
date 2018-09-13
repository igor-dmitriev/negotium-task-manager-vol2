package com.negotium.reminder.data;

import com.negotium.reminder.meta.ReminderStatus;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReminderCriteria {
  private String user;
  private Long taskId;
  private ReminderStatus status;
  private LocalDateTime fromDate;
  private LocalDateTime toDate;
}
