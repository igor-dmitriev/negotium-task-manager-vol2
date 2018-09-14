package com.negotium.reminder.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.negotium.reminder.migration.DynamoDbMigration;
import com.negotium.reminder.repository.DynamoDbReminderRepository;
import com.negotium.reminder.repository.ReminderRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;

@Configuration
@Import(AwsConfig.class)
public class RepositoryConfig {
  private final DynamoDBMapper mapper;

  public RepositoryConfig(DynamoDBMapper mapper) {
    this.mapper = mapper;
  }

  @Bean
  public ReminderRepository reminderRepository() {
    return new DynamoDbReminderRepository(mapper);
  }

  @Bean
  public DynamoDbMigration dynamoDbMigration(
      AmazonDynamoDB dynamoDB,
      @Value("${app.aws.dynamoDb.readCapacityUnits}") long dynamoDbReadCapacityUnits,
      @Value("${app.aws.dynamoDb.writeCapacityUnits}") long dynamoDbWriteCapacityUnits
  ) {
    return new DynamoDbMigration(dynamoDB, dynamoDbReadCapacityUnits, dynamoDbWriteCapacityUnits);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady(ApplicationReadyEvent event) {
    ConfigurableApplicationContext context = event.getApplicationContext();
    context.getBean(DynamoDbMigration.class).migrate();
  }
}
