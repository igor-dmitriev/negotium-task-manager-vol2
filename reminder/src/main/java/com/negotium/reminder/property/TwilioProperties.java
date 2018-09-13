package com.negotium.reminder.property;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "twilio")
@Getter
@Setter
public class TwilioProperties {
  private String accountSid;
  private String authToken;
  private String phoneFrom;
  private String phoneTo;
  private String twimUrl;
}
