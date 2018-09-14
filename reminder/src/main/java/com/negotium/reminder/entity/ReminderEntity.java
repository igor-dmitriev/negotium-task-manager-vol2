package com.negotium.reminder.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.negotium.reminder.meta.ReminderStatus;
import com.negotium.reminder.meta.ReminderType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DynamoDBTable(tableName = ReminderEntity.REMINDERS_TABLE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "user")
public class ReminderEntity {

  public static final String REMINDERS_TABLE = "reminders";
  public static final String USER_ATTR = "user";
  public static final String TASK_ID_ATTR = "task_id";
  public static final String REMIND_AT_ATTR = "remind_at";
  public static final String REMIND_TYPE_ATTR = "type";
  public static final String STATUS_ATTR = "status";
  public static final String ERROR_MSG_ATTR = "error_message";

  @DynamoDBHashKey(attributeName = USER_ATTR)
  private String user;

  @DynamoDBAttribute(attributeName = TASK_ID_ATTR)
  private long taskId;

  @DynamoDBRangeKey(attributeName = REMIND_AT_ATTR)
  private long remindAt;

  @DynamoDBAttribute(attributeName = REMIND_TYPE_ATTR)
  @DynamoDBTypeConvertedEnum
  private ReminderType type;

  @DynamoDBAttribute(attributeName = STATUS_ATTR)
  @DynamoDBTypeConvertedEnum
  @Builder.Default
  private ReminderStatus status = ReminderStatus.SCHEDULED;

  @DynamoDBAttribute(attributeName = ERROR_MSG_ATTR)
  private String errorMessage;
}
