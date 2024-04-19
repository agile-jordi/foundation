package com.agilogy.foundation.java.apiserver;

import com.agilogy.foundation.java.domain.TimeEntriesRepository;
import com.agilogy.foundation.java.domain.TimeEntry;
import com.agilogy.foundation.java.domain.TimeTracking;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import io.javalin.apibuilder.EndpointGroup;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class App {

  public static void main(final String[] args) {
    final var timeTracking = testTimeTracking();
    final var timeTrackingAppAdapter = new TimeTrackingAppAdapter(timeTracking);

    final EndpointGroup serverEndpoints = () -> {
      get("/", ctx -> ctx.redirect("/app/"));
      path("app", timeTrackingAppAdapter.endpointGroup());
    };

    var javalin = Javalin.create(cfg -> {
      cfg.staticFiles.add("web");
      cfg.router.apiBuilder(serverEndpoints);
    });
    timeTrackingAppAdapter.handledExceptions.forEach(javalin::exception);
    if (!timeTrackingAppAdapter.handledExceptions.containsKey(Exception.class)) {
      javalin.exception(Exception.class, (e, ctx) -> {
        e.printStackTrace();
        ctx.result("Internal server error").status(500);
      });
    }
    javalin.start(8080);

  }

  @NotNull private static TimeTracking testTimeTracking() {
    final var repository = new TimeEntriesRepository() {

      private static final @NotNull List<TimeEntry> timeEntriesDemo = List.of(
            new TimeEntry(
                  "Jordi",
                  "Foundation",
                  Instant.parse("2021-01-01T10:00:00Z"),
                  Instant.parse("2021-01-01T11:00:00Z")
            ),
            new TimeEntry(
                  "Jordi",
                  "Foundation",
                  Instant.parse("2021-01-01T10:00:00Z"),
                  Instant.parse("2021-01-01T11:00:00Z")
            )
      );

      private final @NotNull List<TimeEntry> entries = new LinkedList<>(timeEntriesDemo);

      @Override public void saveTimeEntries(List<TimeEntry> entries) {
        System.out.println("Adding time entries: " +
                           entries.stream().reduce("", (s, e) -> s + e.toString() + ", ", String::concat));
        this.entries.addAll(entries);
      }

      @Override public @NotNull List<TimeEntry> listTimeEntries() {
        return List.copyOf(entries).stream().sorted(Comparator.comparing(TimeEntry::start)).toList();
      }
    };
    return new TimeTracking(repository);
  }


}