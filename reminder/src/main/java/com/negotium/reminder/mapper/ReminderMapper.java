package com.negotium.reminder.mapper;

import com.negotium.reminder.dto.ReminderDto;
import com.negotium.reminder.entity.ReminderEntity;
import com.negotium.reminder.parser.RemindAtParser;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Mapper(
    config = MappingConfig.class
)
public abstract class ReminderMapper {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

  @Autowired
  private RemindAtParser remindAtParser;

  @Mapping(target = "remindAt", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "errorMessage", ignore = true)
  public abstract ReminderEntity map(ReminderDto reminderDto);

  @Mapping(target = "remindAt", ignore = true)
  public abstract ReminderDto map(ReminderEntity reminder);

  @AfterMapping
  public void after(@MappingTarget ReminderEntity entity, ReminderDto dto) {
    entity.setRemindAt(remindAtParser.parse(dto.getRemindAt()).toInstant(ZoneOffset.UTC).toEpochMilli());
  }

  @AfterMapping
  public void after(@MappingTarget ReminderDto dto, ReminderEntity entity) {
    dto.setRemindAt(DATE_FORMATTER.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getRemindAt()), ZoneOffset.UTC)));
  }
}
