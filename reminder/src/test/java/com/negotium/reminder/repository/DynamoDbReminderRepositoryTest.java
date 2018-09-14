package com.negotium.reminder.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.negotium.reminder.config.ReminderRepositoryTestConf;
import com.negotium.reminder.entity.ReminderEntity;
import com.negotium.reminder.meta.ReminderStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ReminderRepositoryTestConf.class, initializers = DynamoDbReminderRepositoryTest.Initializer.class)
public class DynamoDbReminderRepositoryTest {

  private static final int DYNAMODB_PORT = 4569;

  @Autowired
  AmazonDynamoDB dynamoDb;

  @Autowired
  ReminderRepository repository;

  private static final String USER = "Igor";

  @ClassRule
  public static final LocalStackContainer DYNAMODB_CONTAINER = new LocalStackContainer().withServices(DYNAMODB);

  @Before
  public void setUpClass() {
    ensureDynamoDb();
  }

  @After
  public void cleanUp() {
    repository.deleteAll();
  }

  @Test
  public void shouldSaveReminder() {
    // given
    ReminderEntity reminder = ReminderEntity.builder()
        .user(USER)
        .status(ReminderStatus.SCHEDULED)
        .build();
    repository.save(reminder);

    // when
    Optional<ReminderEntity> saved = repository.findById(USER);

    // then
    assertThat(saved).isEqualTo(Optional.of(reminder));
  }

  private void ensureDynamoDb() {
    while (true) {
      try {
        boolean isDynamoDbCreated = dynamoDb.describeTable(ReminderEntity.REMINDERS_TABLE).getTable().getTableStatus().equals("ACTIVE");
        if (!isDynamoDbCreated) {
          createDynamoDbTable();
        } else {
          break;
        }
      } catch (ResourceNotFoundException e) {
        createDynamoDbTable();
      }
    }
  }

  private void createDynamoDbTable() {
    CreateTableRequest createTableRequest = new CreateTableRequest()
        .withTableName(ReminderEntity.REMINDERS_TABLE)
        .withKeySchema(
            new KeySchemaElement().withAttributeName(ReminderEntity.USER_ATTR).withKeyType(KeyType.HASH),
            new KeySchemaElement().withAttributeName(ReminderEntity.REMIND_AT_ATTR).withKeyType(KeyType.RANGE)
        )
        .withAttributeDefinitions(
            new AttributeDefinition().withAttributeName(ReminderEntity.USER_ATTR).withAttributeType(ScalarAttributeType.S),
            new AttributeDefinition().withAttributeName(ReminderEntity.REMIND_AT_ATTR).withAttributeType(ScalarAttributeType.N)
        )
        .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
    dynamoDb.createTable(createTableRequest);
  }

  static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
      EnvironmentTestUtils.addEnvironment("testcontainers", ctx.getEnvironment(),
          "aws.dynamodb.endpoint=" + createUrl(DYNAMODB_CONTAINER.getContainerIpAddress(), DYNAMODB_CONTAINER.getMappedPort(DYNAMODB_PORT))
      );
    }

    private static String createUrl(String host, Integer port) {
      return String.format("http://%s:%d", host, port);
    }
  }
}
