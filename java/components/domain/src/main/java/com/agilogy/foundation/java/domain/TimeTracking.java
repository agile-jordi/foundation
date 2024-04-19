package com.agilogy.foundation.java.domain;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TimeTracking {

  private final @NotNull TimeEntriesRepository repository;

  public TimeTracking(final @NotNull TimeEntriesRepository repository) {
    this.repository = repository;
  }

  public final void addTimeEntries(final @NotNull List<TimeEntry> entries){
    // TODO: Validate entries (e.g. that the entries are not overlapping)
    repository.saveTimeEntries(entries);
  }
  public final @NotNull List<TimeEntry> listTimeEntries(){
    return repository.listTimeEntries();
  }
}
