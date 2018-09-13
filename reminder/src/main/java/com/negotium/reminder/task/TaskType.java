package com.negotium.reminder.task;

import com.negotium.reminder.meta.ReminderType;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER, ElementType.METHOD})
public @interface TaskType {
  ReminderType value();
}
