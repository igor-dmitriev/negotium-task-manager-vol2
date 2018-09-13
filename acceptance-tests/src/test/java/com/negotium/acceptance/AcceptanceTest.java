package com.negotium.acceptance;


import com.jayway.jsonpath.JsonPath;

import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
public class AcceptanceTest {

  private static final String TASK_SERVICE_NAME = "task_1";
  private static final String REMINER_SERVICE_NAME = "reminder_1";

  private static String taskHost;
  private static int taskPort;

  private static String reminderHost;
  private static int reminderPort;

  private static final int DEFAULT_TASK_SERVICE_PORT = 8000;
  private static final int DEFAULT_REMINDER_SERVICE_PORT = 8001;

  @ClassRule
  public static DockerComposeContainer CONTAINERS = new DockerComposeContainer(new File("../docker-compose.yml"))
      .withLocalCompose(true)
      .withPull(false)
      .withExposedService(TASK_SERVICE_NAME, DEFAULT_TASK_SERVICE_PORT)
      .withExposedService(REMINER_SERVICE_NAME, DEFAULT_REMINDER_SERVICE_PORT);

  @BeforeClass
  public static void setUpClass() throws InterruptedException {
    // TODO wait until all services is up, implement health check wait - https://github.com/testcontainers/testcontainers-java/issues/174
    Thread.sleep(5_000);

    taskHost = CONTAINERS.getServiceHost(TASK_SERVICE_NAME, DEFAULT_TASK_SERVICE_PORT);
    taskPort = CONTAINERS.getServicePort(TASK_SERVICE_NAME, DEFAULT_TASK_SERVICE_PORT);

    reminderHost = CONTAINERS.getServiceHost(REMINER_SERVICE_NAME, DEFAULT_REMINDER_SERVICE_PORT);
    reminderPort = CONTAINERS.getServicePort(REMINER_SERVICE_NAME, DEFAULT_REMINDER_SERVICE_PORT);
  }

  @Test
  public void createReminderForTask() {
    String task = "{\n" +
        "  \"description\": \"\"\n" +
        "}";

    String createdTaskJson = createTask(task);
    int taskId = JsonPath.read(createdTaskJson, "$.id");

    String reminderJson = "{\n" +
        "  \"taskId\": \" " + taskId + " \",\n" +
        "  \"remindAt\": \"через 1 минуту\",\n" +
        "  \"type\": \"SMS\"\n" +
        "}";

    String createdReminderJson = createReminder(reminderJson);

    assertThat(createdReminderJson, hasJsonPath("$.taskId", equalTo(taskId)));
    assertThat(createdReminderJson, hasJsonPath("$.type", equalTo("SMS")));
    assertThat(createdReminderJson, hasJsonPath("$.status", equalTo("SCHEDULED")));
    assertThat(createdReminderJson, hasJsonPath("$.remindAt"));
  }

  private String createReminder(String reminderPayload) {
    return RestAssured.given()
        .contentType(ContentType.JSON)
        .when()
        .body(reminderPayload)
        .post(createUri(reminderHost, reminderPort) + "/reminders")
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .extract()
        .body()
        .asString();
  }

  private String createTask(String taskPayload) {
    return RestAssured.given()
        .contentType(ContentType.JSON)
        .when()
        .body(taskPayload)
        .post(createUri(taskHost, taskPort) + "/tasks")
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .extract()
        .body()
        .asString();
  }

  private static String createUri(String host, Integer port) {
    return String.format("http://%s:%d", host, port);
  }
}
