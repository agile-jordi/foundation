package com.agilogy.foundation.java.domain;

import javautils.NotNull;

public interface TimeTracking {

  void addTimeEntries(@NotNull final TimeEntry... entries);
}
