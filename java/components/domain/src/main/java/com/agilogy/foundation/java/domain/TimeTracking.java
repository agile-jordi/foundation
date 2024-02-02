package com.agilogy.foundation.java.domain;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TimeTracking {

  void addTimeEntries(final @NotNull List<TimeEntry> entries);
  @NotNull List<TimeEntry> listTimeEntries();
}
