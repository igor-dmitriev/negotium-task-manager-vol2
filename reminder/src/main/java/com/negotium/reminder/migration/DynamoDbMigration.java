package com.negotium.reminder.migration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.TimeToLiveSpecification;
import com.amazonaws.services.dynamodbv2.model.UpdateTimeToLiveRequest;
import com.negotium.reminder.entity.ReminderEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DynamoDbMigration {
  private final AmazonDynamoDB dynamoDb;
  private final long dynamoDbReadCapacityUnits;
  private final long dynamoDbWriteCapacityUnits;

  public void migrate() {
    try {
      DescribeTableResult describeResult = dynamoDb.describeTable(ReminderEntity.REMINDERS_TABLE);
      TableDescription table = describeResult.getTable();
      log.info("{} table description:", ReminderEntity.REMINDERS_TABLE);
      log.info("AttributeDefinitions:" + table.getAttributeDefinitions());
      log.info("Key Schema:" + table.getKeySchema());
      log.info("Provisioned Throughput Read Capacity Units: " + table.getProvisionedThroughput().getReadCapacityUnits());
      log.info("Provisioned Throughput Write Capacity Units: " + table.getProvisionedThroughput().getWriteCapacityUnits());
    } catch (ResourceNotFoundException e) {
      log.info("{} table not found", ReminderEntity.REMINDERS_TABLE);
      log.info("Creating the {} table ...", ReminderEntity.REMINDERS_TABLE);
      dynamoDb.createTable(createReminderTableRequest());
      dynamoDb.updateTimeToLive(timeToLiveRequest());
    }
  }

  private UpdateTimeToLiveRequest timeToLiveRequest() {
    return new UpdateTimeToLiveRequest()
        .withTableName(ReminderEntity.REMINDERS_TABLE)
        .withTimeToLiveSpecification(
            new TimeToLiveSpecification()
                .withAttributeName(ReminderEntity.REMIND_AT_ATTR)
                .withEnabled(true)
        );
  }

  private CreateTableRequest createReminderTableRequest() {
    return new CreateTableRequest()
        .withTableName(ReminderEntity.REMINDERS_TABLE)
        .withAttributeDefinitions(
            new AttributeDefinition().withAttributeName(ReminderEntity.USER_ATTR).withAttributeType(ScalarAttributeType.S),
            new AttributeDefinition().withAttributeName(ReminderEntity.REMIND_AT_ATTR).withAttributeType(ScalarAttributeType.N)
        )
        .withKeySchema(
            new KeySchemaElement().withAttributeName(ReminderEntity.USER_ATTR).withKeyType(KeyType.HASH),
            new KeySchemaElement().withAttributeName(ReminderEntity.REMIND_AT_ATTR).withKeyType(KeyType.RANGE)
        ).withProvisionedThroughput(
            new ProvisionedThroughput()
                .withReadCapacityUnits(dynamoDbReadCapacityUnits)
                .withWriteCapacityUnits(dynamoDbWriteCapacityUnits)
        );
  }
}
