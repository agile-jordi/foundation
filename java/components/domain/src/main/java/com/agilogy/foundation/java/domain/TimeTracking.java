package com.agilogy.foundation.java.domain;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TimeTracking {

  void addTimeEntries(@NotNull final TimeEntry... entries);
  List<TimeEntry> listTimeEntries();
}
