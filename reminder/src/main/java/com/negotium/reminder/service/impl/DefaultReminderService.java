package com.negotium.reminder.service.impl;

import com.negotium.reminder.data.ReminderCriteria;
import com.negotium.reminder.entity.ReminderEntity;
import com.negotium.reminder.meta.ReminderStatus;
import com.negotium.reminder.repository.ReminderRepository;
import com.negotium.reminder.service.ReminderService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class DefaultReminderService implements ReminderService {

  private static final LocalDateTime ONE_HUNDRED_YEARS_BACK = LocalDateTime.now(Clock.systemUTC()).minusYears(100);
  private static final LocalDateTime ONE_HUNDRED_YEARS_FORWARD = LocalDateTime.now(Clock.systemUTC()).plusYears(100);

  @Value("${app.config.user}")
  private String user;

  private final ReminderRepository reminderRepository;

  @Override
  public ReminderEntity save(ReminderEntity reminder) {
    reminder.setUser(user);
    reminderRepository.save(reminder);
    return reminder;
  }

  @Override
  public List<ReminderEntity> getAllScheduled() {
    LocalDateTime startOfTheCurrentMinute = LocalDateTime.now(Clock.systemUTC()).withSecond(0).withNano(0);
    LocalDateTime endOfTheCurrentMinute = LocalDateTime.now(Clock.systemUTC()).withSecond(59).withNano(999_999_999);
    /*return reminderRepository.findReminders(
        ReminderCriteria.builder().user(user)
            .status(ReminderStatus.SCHEDULED)
            .fromDate(startOfTheCurrentMinute)
            .toDate(endOfTheCurrentMinute)
            .build()
    );*/
    return Collections.emptyList();
  }

  @Override
  public void update(ReminderEntity reminder) {
    reminderRepository.save(reminder);
  }

  @Override
  public List<ReminderEntity> getForTask(Long taskId) {
    ReminderCriteria.ReminderCriteriaBuilder searchCriteriaBuilder = ReminderCriteria.builder()
        .user(user)
        .taskId(taskId)
        .status(ReminderStatus.SCHEDULED)
        .fromDate(ONE_HUNDRED_YEARS_BACK)
        .toDate(ONE_HUNDRED_YEARS_FORWARD);
    //return reminderRepository.findReminders(searchCriteriaBuilder.build());
    return Collections.emptyList();
  }
}
