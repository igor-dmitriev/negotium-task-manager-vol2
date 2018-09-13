package com.negotium.reminder.helper;

public interface ApiErrorsFactory {
  ApiErrors createBadRequest(String message);

  ApiErrors createInternalServerError();

  ApiErrors createNotFound(String message);

  ApiErrors createUnprocessable(String message);
}
