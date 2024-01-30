package com.agilogy.foundation.java.apiserver;

import com.agilogy.foundation.java.domain.TimeEntry;
import com.agilogy.foundation.java.domain.TimeTracking;

import java.util.Arrays;

public class App {

  public static void main(final String[] args) {
    final var timeTracking = new TimeTracking() {
      @Override public void addTimeEntries(TimeEntry... entries) {
        System.out.println("Adding time entries: " +
                           Arrays.stream(entries).reduce("", (s, e) -> s + e.employeeId() + ", ", String::concat));
      }
    };
    final var timeTrackingAdapter = new TimeTrackingAdapter(timeTracking);
    timeTrackingAdapter.start(8080, false);
  }

}