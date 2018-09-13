package com.negotium.reminder;


import com.negotium.reminder.config.ReminderConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Import(ReminderConfig.class)
public class ReminderApplication {
  public static void main(String[] args) {
    SpringApplication.run(ReminderApplication.class);
  }
}
