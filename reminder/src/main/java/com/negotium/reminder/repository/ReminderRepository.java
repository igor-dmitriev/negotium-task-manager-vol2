package com.negotium.reminder.repository;

import com.negotium.reminder.data.ReminderCriteria;
import com.negotium.reminder.entity.ReminderEntity;

import java.util.List;
import java.util.Optional;

public interface ReminderRepository {
  List<ReminderEntity> findReminders(ReminderCriteria search);

  Optional<ReminderEntity> findOne(String user);

  void save(ReminderEntity reminder);

  int count(String user);

  void update(ReminderEntity reminder);

  void deleteAll();
}
