package com.negotium.task.controller;

import com.negotium.common.Endpoints;
import com.negotium.common.meta.TaskStatus;
import com.negotium.task.dto.TaskDto;
import com.negotium.task.entity.TaskEntity;
import com.negotium.task.mapper.TaskMapper;
import com.negotium.task.service.TaskService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(Endpoints.TASKS)
@RequiredArgsConstructor
@CrossOrigin
public class TaskController {

  private final TaskService service;

  @GetMapping
  public ResponseEntity<List<TaskDto>> getAll(@RequestParam(value = "status", required = false) TaskStatus status) {
    List<TaskDto> tasks = service.getAll(status).stream()
        .map(TaskMapper::map)
        .collect(toList());
    return ResponseEntity.status(HttpStatus.OK).body(tasks);
  }

  @GetMapping(Endpoints.ID)
  public ResponseEntity<TaskDto> get(@PathVariable Long id) {
    TaskDto task = TaskMapper.map(service.get(id));
    return ResponseEntity.status(HttpStatus.OK).body(task);
  }

  @PostMapping
  public ResponseEntity<TaskDto> add(@RequestBody @Valid TaskDto taskDto) {
    TaskEntity created = service.save(TaskMapper.map(taskDto));
    return ResponseEntity.status(HttpStatus.CREATED).body(TaskMapper.map(created));
  }

  @PutMapping("/{id}/complete")
  public ResponseEntity complete(@PathVariable("id") Long id) {
    service.completeTask(id);
    return ResponseEntity.ok().build();
  }
}
