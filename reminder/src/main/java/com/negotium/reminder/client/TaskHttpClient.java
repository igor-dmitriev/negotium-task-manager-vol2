package com.negotium.reminder.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.negotium.common.Endpoints;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

@RequiredArgsConstructor
public class TaskHttpClient {

  @Value("${app.config.task-endpoint}")
  private String taskEndpoint;

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  public boolean isTaskActive(Long taskId) {
    return getTask(taskId).get("status").equals("ACTIVE");
  }

  private HashMap getTask(Long taskId) {
    String json = restTemplate.getForObject(taskEndpoint + "/" + Endpoints.TASKS + "/" + taskId, String.class);
    try {
      return objectMapper.readValue(json, HashMap.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getTaskStatus(Long taskId) {
    return String.valueOf(getTask(taskId).get("status"));
  }

  public String getTaskDescription(Long taskId) {
    return String.valueOf(getTask(taskId).get("description"));
  }
}
