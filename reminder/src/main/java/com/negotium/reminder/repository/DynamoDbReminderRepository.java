package com.negotium.reminder.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.negotium.reminder.data.ReminderCriteria;
import com.negotium.reminder.entity.ReminderEntity;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.time.ZoneOffset;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DynamoDbReminderRepository implements ReminderRepository {

  private final DynamoDBMapper mapper;

  @Override
  public List<ReminderEntity> findReminders(ReminderCriteria search) {
    DynamoDBQueryExpression<ReminderEntity> queryExpression = new DynamoDBQueryExpression<>();
    queryExpression.setScanIndexForward(false);

    queryExpression.withHashKeyValues(
        ReminderEntity.builder().user(search.getUser()).build()
    );

    if (search.getStatus() != null) {
      queryExpression.withQueryFilterEntry(ReminderEntity.STATUS_ATTR, new Condition()
          .withComparisonOperator(ComparisonOperator.EQ)
          .withAttributeValueList(new AttributeValue().withS(search.getStatus().name()))
      );
    }

    if (search.getTaskId() != null) {
      queryExpression.withQueryFilterEntry(ReminderEntity.TASK_ID_ATTR, new Condition()
          .withComparisonOperator(ComparisonOperator.EQ)
          .withAttributeValueList(new AttributeValue().withN(search.getTaskId().toString()))
      );
    }

    queryExpression.withRangeKeyCondition(ReminderEntity.REMIND_AT_ATTR, new Condition()
        .withComparisonOperator(ComparisonOperator.BETWEEN)
        .withAttributeValueList(
            new AttributeValue().withN(String.format("%d", search.getFromDate().toInstant(ZoneOffset.UTC).toEpochMilli())),
            new AttributeValue().withN(String.format("%d", search.getToDate().toInstant(ZoneOffset.UTC).toEpochMilli()))
        ));

    return mapper.query(ReminderEntity.class, queryExpression);
  }

  @Override
  public ReminderEntity findOne(String user) {
    return mapper.load(ReminderEntity.class, user);
  }

  @Override
  public void save(ReminderEntity reminder) {
    mapper.save(reminder);
  }

  @Override
  public int count(String user) {
    DynamoDBQueryExpression<ReminderEntity> queryExpression = new DynamoDBQueryExpression<>();
    queryExpression.withHashKeyValues(ReminderEntity.builder().user(user).build());
    return mapper.count(ReminderEntity.class, queryExpression);
  }

  @Override
  public void update(ReminderEntity reminder) {
    mapper.save(reminder);
  }

  @Override
  public void deleteAll() {
    mapper.scan(ReminderEntity.class, new DynamoDBScanExpression()).forEach(mapper::delete);
  }
}
