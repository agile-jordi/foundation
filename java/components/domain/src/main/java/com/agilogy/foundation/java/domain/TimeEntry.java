package com.agilogy.foundation.java.domain;

import javautils.NotNull;

import java.time.Instant;

public record TimeEntry(
      @NotNull String employeeId,
      @NotNull String projectId,
      @NotNull Instant start,
      @NotNull Instant end
) {
}
