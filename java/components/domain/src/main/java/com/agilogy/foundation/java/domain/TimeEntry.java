package com.agilogy.foundation.java.domain;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public record TimeEntry(
      @NotNull String employeeId,
      @NotNull String projectId,
      @NotNull Instant start,
      @NotNull Instant end
) {

  public @NotNull LocalDate startDate() {
    return start.atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public @NotNull LocalTime startTime() {
    return start.atZone(ZoneId.systemDefault()).toLocalTime();
  }

  public @NotNull Duration duration() {
    return Duration.between(start, end);
  }

  public @NotNull String humanDuration() {
    return String.format("%d:%02d", duration().toHours(), duration().toMinutesPart());
  }

  public TimeEntry roundToMinutes() {
    return new TimeEntry(
          employeeId,
          projectId,
          start.truncatedTo(ChronoUnit.MINUTES),
          end.truncatedTo(ChronoUnit.MINUTES)
    );
  }
}



