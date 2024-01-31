package com.agilogy.foundation.java.apiserver;

import com.agilogy.foundation.java.domain.TimeTracking;
import static com.agilogy.javalin.JavalinUtils.html;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import io.javalin.apibuilder.EndpointGroup;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javautils.JavaIO.readInputStream;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class TimeTrackingAppAdapter {

  private final @NotNull TimeTracking timeTracking;

  public TimeTrackingAppAdapter(@NotNull final TimeTracking timeTracking) {
    this.timeTracking = timeTracking;
  }

  public final @NotNull EndpointGroup endpointGroup = () -> {
    get(ctx -> html(ctx, "index.html"));
    path("/uploadTimeEntries", () -> {
      get(ctx -> html(ctx, "uploadTimeEntriesForm.html"));
      post(ctx -> {
        var file = ctx.uploadedFile("timeEntries");
        if (file == null) {
          ctx.result("File timeEntries is missing").status(400);
        } else if (!Objects.equals(file.contentType(), "text/csv")) {
          ctx.result("File timeEntries is not a CSV file").status(400);
        } else {
          var timeEntries = file.contentAndClose(is -> new String(readInputStream(is), UTF_8));
          System.out.println(timeEntries);
          ctx.redirect("/app");
        }
      });
    });
  };


}
