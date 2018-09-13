package com.negotium.reminder.helper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ApiErrors {
  private final List<ApiError> errors;
}
