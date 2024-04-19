package com.agilogy.foundation.java.domain;

import java.util.List;

public interface TimeEntriesRepository {
  void saveTimeEntries(List<TimeEntry> entries);

  List<TimeEntry> listTimeEntries();
}
