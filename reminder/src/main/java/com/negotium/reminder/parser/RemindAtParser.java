package com.negotium.reminder.parser;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemindAtParser {

  private static final String VALUE_GROUP = "value";
  private final Pattern pattern;

  public RemindAtParser(String pattern) {
    this.pattern = Pattern.compile(pattern);
  }

  public LocalDateTime parse(String remindAt) {
    Matcher matcher = pattern.matcher(remindAt);
    if (!matcher.find()) {
      throw new IllegalArgumentException("Unknown remind at pattern: " + remindAt);
    }
    int minutes = Integer.parseInt(matcher.group(VALUE_GROUP));
    return LocalDateTime.now(ZoneOffset.UTC).plusMinutes(minutes);
  }
}
