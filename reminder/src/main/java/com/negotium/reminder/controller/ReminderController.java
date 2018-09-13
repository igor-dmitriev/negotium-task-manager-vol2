package com.negotium.reminder.controller;


import com.negotium.common.Endpoints;
import com.negotium.common.meta.TaskStatus;
import com.negotium.reminder.client.TaskHttpClient;
import com.negotium.reminder.dto.ReminderDto;
import com.negotium.reminder.error.IllegalTaskStateException;
import com.negotium.reminder.mapper.ReminderMapper;
import com.negotium.reminder.service.ReminderService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(Endpoints.REMINDERS)
@RequiredArgsConstructor
@CrossOrigin
public class ReminderController {

  private final ReminderService service;
  private final ReminderMapper mapper;
  private final TaskHttpClient taskHttpClient;

  @GetMapping
  public ResponseEntity<List<ReminderDto>> getForTask(@RequestParam(value = "taskId") Long taskId) {
    List<ReminderDto> taskReminders = service.getForTask(taskId).stream().map(mapper::map).collect(toList());
    return ResponseEntity.ok(taskReminders);
  }

  @PostMapping
  public ResponseEntity<ReminderDto> add(@RequestBody @Valid ReminderDto reminderDto) {
    Long taskId = reminderDto.getTaskId();
    if (TaskStatus.valueOf(taskHttpClient.getTaskStatus(taskId)) != TaskStatus.ACTIVE) {
      throw new IllegalTaskStateException(taskId);
    }

    ReminderDto dto = mapper.map(service.save(mapper.map(reminderDto)));
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }
}
