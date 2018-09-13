package com.negotium.reminder.controller;

import com.negotium.common.error.InvalidPayloadException;
import com.negotium.common.error.ResourceNotFoundException;
import com.negotium.reminder.error.IllegalTaskStateException;
import com.negotium.reminder.helper.ApiError;
import com.negotium.reminder.helper.ApiErrors;
import com.negotium.reminder.helper.ApiErrorsFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Igor Dmitriev on 2/21/18
 */
@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestExceptionHandler {

  private final ApiErrorsFactory apiErrorsFactory;

  @ExceptionHandler(value = {Exception.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ApiErrors unhandledException(Exception ex) {
    log.error(ex.getMessage(), ex);
    return apiErrorsFactory.createInternalServerError();
  }

  @ExceptionHandler(value = {ResourceNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ApiErrors resourceNotFoundException(ResourceNotFoundException ex) {
    return apiErrorsFactory.createNotFound(ex.getMessage());
  }

  @ExceptionHandler(value = {IllegalTaskStateException.class})
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ResponseBody
  public ApiErrors resourceNotFoundException(IllegalTaskStateException ex) {
    return apiErrorsFactory.createUnprocessable(ex.getMessage());
  }

  @ExceptionHandler({InvalidPayloadException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ApiErrors invalidPayloadException(InvalidPayloadException ex) {
    List<ApiError> listOfErrors = ex.getMessages().stream()
        .map(apiErrorsFactory::createBadRequest)
        .map(ApiErrors::getErrors)
        .flatMap(List::stream)
        .collect(Collectors.toList());
    return new ApiErrors(listOfErrors);
  }

}
