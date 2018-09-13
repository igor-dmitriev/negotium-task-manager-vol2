package com.negotium.reminder.task;

import com.negotium.reminder.entity.ReminderEntity;
import com.negotium.reminder.meta.ReminderType;
import com.negotium.reminder.property.TwilioProperties;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
@TaskType(ReminderType.PHONE_CALL)
public class PhoneCallTask implements Task {

  private final TwilioProperties twilioProperties;

  @PostConstruct
  public void init() {
    Twilio.init(twilioProperties.getAccountSid(), twilioProperties.getAuthToken());
  }

  @Override
  public void execute(ReminderEntity reminder) {
    try {
      Call call = Call.creator(new PhoneNumber(twilioProperties.getPhoneTo()), new PhoneNumber(twilioProperties.getPhoneFrom()), new URI(twilioProperties.getTwimUrl())).create();
      log.debug("Calling the phone, from: {} to: {}, sid: {}", twilioProperties.getPhoneFrom(), twilioProperties.getPhoneTo(), call.getSid());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
