package com.negotium.task.repository;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.negotium.common.meta.TaskStatus;
import com.negotium.task.entity.TaskEntity;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = TaskRepositoryTest.Initializers.class)
public class TaskRepositoryTest {

  @ClassRule
  public static final PostgreSQLContainer POSTGRESQL_CONTAINER = new PostgreSQLContainer();

  @Autowired
  DataSource dataSource;

  @Rule
  public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> dataSource.getConnection());

  @Autowired
  TaskRepository taskRepository;

  @Test
  @DataSet("/tasks.xml")
  public void returnOnlyActiveTasks() {
    List<TaskEntity> tasks = taskRepository.findByStatus(TaskStatus.ACTIVE);
    assertThat(tasks).hasSize(2);
    assertThat(tasks.stream().allMatch(TaskEntity::isActive)).isTrue();
  }

  static class Initializers implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
      EnvironmentTestUtils.addEnvironment("testcontainers", ctx.getEnvironment(),
          "spring.datasource.url=" + POSTGRESQL_CONTAINER.getJdbcUrl(),
          "spring.datasource.username=" + POSTGRESQL_CONTAINER.getUsername(),
          "spring.datasource.password=" + POSTGRESQL_CONTAINER.getPassword(),
          "spring.datasource.driverClassName=" + "org.postgresql.Driver"
      );
    }
  }
}
