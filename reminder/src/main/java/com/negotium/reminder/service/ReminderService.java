package com.negotium.reminder.service;

import com.negotium.reminder.entity.ReminderEntity;

import java.util.List;

public interface ReminderService {
  ReminderEntity save(ReminderEntity reminder);

  List<ReminderEntity> getAllScheduled();

  void update(ReminderEntity reminder);

  List<ReminderEntity> getForTask(Long taskId);
}
