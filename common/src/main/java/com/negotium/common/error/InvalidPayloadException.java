package com.negotium.common.error;

import java.util.List;

public class InvalidPayloadException extends RuntimeException implements MultiMessages {

  private final List<String> allErrorMessages;

  public InvalidPayloadException(String message, List<String> allErrorMessages) {
    super(message);
    this.allErrorMessages = allErrorMessages;
  }

  @Override
  public String getMessage() {
    return allErrorMessages.toString();
  }

  @Override
  public List<String> getMessages() {
    return allErrorMessages;
  }
}
