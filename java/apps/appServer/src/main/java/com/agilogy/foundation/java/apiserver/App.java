package com.agilogy.foundation.java.apiserver;

import com.agilogy.foundation.java.domain.TimeEntry;
import com.agilogy.foundation.java.domain.TimeTracking;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.path;
import io.javalin.apibuilder.EndpointGroup;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class App {

  public static void main(final String[] args) {
    final var timeTracking = new TimeTracking() {
      @Override public void addTimeEntries(TimeEntry... entries) {
        System.out.println("Adding time entries: " +
                           Arrays.stream(entries).reduce("", (s, e) -> s + e.employeeId() + ", ", String::concat));
      }

      @Override public List<TimeEntry> listTimeEntries() {
        return List.of(new TimeEntry("Jordi", "foundation", Instant.now().minusSeconds(3600), Instant.now()));
      }
    };
    final var timeTrackingAppAdapter = new TimeTrackingAppAdapter(timeTracking);

    final EndpointGroup serverEndpoints = () -> {
      path("app", timeTrackingAppAdapter.endpointGroup);
    };

    Javalin
          .create(cfg -> cfg.router.apiBuilder(serverEndpoints))
          .start(8080);

  }


}