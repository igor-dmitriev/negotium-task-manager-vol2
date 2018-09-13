package com.negotium.reminder.task;

import com.negotium.reminder.client.TaskHttpClient;
import com.negotium.reminder.entity.ReminderEntity;
import com.negotium.reminder.meta.ReminderType;
import com.negotium.reminder.property.TwilioProperties;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@TaskType(ReminderType.SMS)
@Slf4j
public class SmsTask implements Task {

  private final TwilioProperties twilioProperties;
  private final TaskHttpClient taskHttpClient;

  @PostConstruct
  public void init() {
    Twilio.init(twilioProperties.getAccountSid(), twilioProperties.getAuthToken());
  }

  @Override
  public void execute(ReminderEntity reminder) {
    PhoneNumber from = new PhoneNumber(twilioProperties.getPhoneFrom());
    PhoneNumber to = new PhoneNumber(twilioProperties.getPhoneTo());
    Message message = Message.creator(to, from, taskHttpClient.getTaskDescription(reminder.getTaskId())).create();
    log.debug("Sending a message, from: {} to: {}, sid: {}", twilioProperties.getPhoneFrom(), twilioProperties.getPhoneTo(), message.getSid());
  }
}

