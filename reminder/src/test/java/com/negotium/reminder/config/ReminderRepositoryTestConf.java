package com.negotium.reminder.config;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.negotium.reminder.repository.DefaultReminderRepository;
import com.negotium.reminder.repository.ReminderRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AwsConfig.class)
public class ReminderRepositoryTestConf {
  @Bean
  public ReminderRepository reminderRepository(DynamoDBMapper mapper) {
    return new DefaultReminderRepository(mapper);
  }
}
