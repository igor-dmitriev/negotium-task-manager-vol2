package com.negotium.reminder.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.negotium.reminder.client.TaskHttpClient;
import com.negotium.reminder.meta.ReminderType;
import com.negotium.reminder.parser.RemindAtParser;
import com.negotium.reminder.property.TwilioProperties;
import com.negotium.reminder.repository.ReminderRepository;
import com.negotium.reminder.service.ReminderService;
import com.negotium.reminder.service.impl.DefaultReminderService;
import com.negotium.reminder.task.PhoneCallTask;
import com.negotium.reminder.task.SmsTask;
import com.negotium.reminder.task.Task;
import com.negotium.reminder.task.TaskType;
import com.negotium.reminder.worker.ReminderWorker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
@Import({RepositoryConfig.class})
public class ReminderConfig {

  private final ReminderRepository reminderRepository;

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder()
        .setReadTimeout(3_000)
        .build();
  }

  @Bean
  public ReminderService reminderService() {
    return new DefaultReminderService(reminderRepository);
  }

  @Bean
  public TaskHttpClient taskHttpClient() {
    return new TaskHttpClient(restTemplate(), objectMapper());
  }

  @Bean
  public ReminderWorker reminderWorker() {
    return new ReminderWorker(reminderService(), taskHttpClient());
  }

  @Bean
  @TaskType(ReminderType.SMS)
  public Task smsTask() {
    return new SmsTask(twilioProperties(), taskHttpClient());
  }

  @Bean
  @TaskType(ReminderType.PHONE_CALL)
  public Task phoneCallTask() {
    return new PhoneCallTask(twilioProperties());
  }

  @Bean
  public RemindAtParser remindAtParser(@Value("${app.reminder.pattern}") String pattern) {
    return new RemindAtParser(pattern);
  }

  @Bean
  public TwilioProperties twilioProperties() {
    return new TwilioProperties();
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
