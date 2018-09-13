package com.negotium.reminder.task;

import com.negotium.reminder.entity.ReminderEntity;

public interface Task {
  void execute(ReminderEntity reminder);
}
