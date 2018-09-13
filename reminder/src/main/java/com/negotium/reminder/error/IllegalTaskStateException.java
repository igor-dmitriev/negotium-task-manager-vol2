package com.negotium.reminder.error;

public class IllegalTaskStateException extends RuntimeException {
  public IllegalTaskStateException(Long taskId) {
    super("Illegal task state, task must be ACTIVE: " + taskId);
  }
}
