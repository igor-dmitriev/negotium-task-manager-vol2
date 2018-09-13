package com.negotium.task.service;

import com.negotium.task.repository.TaskRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

  @Autowired
  private TaskService taskService;

  @Mock
  private TaskRepository taskRepository;

  @Before
  public void setUp() {
    taskService = new DefaultTaskService(taskRepository);
  }

  @Test
  public void test() {
    assertThat(true, is(true));
  }
}
