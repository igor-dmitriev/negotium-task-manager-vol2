package com.negotium.reminder.worker;

import com.negotium.reminder.client.TaskHttpClient;
import com.negotium.reminder.entity.ReminderEntity;
import com.negotium.reminder.meta.ReminderStatus;
import com.negotium.reminder.meta.ReminderType;
import com.negotium.reminder.service.ReminderService;
import com.negotium.reminder.task.Task;
import com.negotium.reminder.task.TaskType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class ReminderWorker {

  private final ReminderService reminderService;
  private Map<ReminderType, Task> tasks = new EnumMap<>(ReminderType.class);
  private final TaskHttpClient taskHttpClient;

  @Autowired
  public ReminderWorker(
      ReminderService reminderService,
      TaskHttpClient taskHttpClient,
      @TaskType(ReminderType.SMS) Task sms,
      @TaskType(ReminderType.PHONE_CALL) Task phoneCall
  ) {
    this.reminderService = reminderService;
    this.taskHttpClient = taskHttpClient;

    this.tasks.put(ReminderType.SMS, sms);
    this.tasks.put(ReminderType.PHONE_CALL, phoneCall);
  }

  @Scheduled(cron = "${app.job.scheduler.execution.repeat}")
  public void doWork() {
    List<ReminderEntity> reminders = reminderService.getAllScheduled();
    log.debug("Found scheduled {} reminders", reminders.size());
    reminders.forEach(this::execute);
  }

  private void execute(ReminderEntity reminder) {
    try {
      Task task = tasks.get(reminder.getType());
      if (task == null) {
        throw new IllegalArgumentException("Unknown reminder type: " + reminder.getType());
      }

      boolean isTaskActive = taskHttpClient.isTaskActive(reminder.getTaskId());
      if (isTaskActive) {
        log.debug("Execute task {}", reminder.getTaskId());
        task.execute(reminder);
        reminder.setStatus(ReminderStatus.EXECUTED);
      } else {
        log.debug("Task {} is inactive", reminder.getTaskId());
        reminder.setStatus(ReminderStatus.IRRELEVANT);
      }
    } catch (Exception e) {
      reminder.setStatus(ReminderStatus.FAILED);
      reminder.setErrorMessage(ExceptionUtils.getStackTrace(e));
      log.error("Failed to execute task {}, error: {}", reminder.getTaskId(), e);
    }
    reminderService.update(reminder);
  }
}
