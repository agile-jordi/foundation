package com.agilogy.foundation.java.apiserver;

import static com.agilogy.foundation.java.apiserver.TimeEntriesCsvReader.readTimeEntriesCsv;
import com.agilogy.foundation.java.domain.TimeTracking;
import com.agilogy.javalin.HttpException;
import static com.agilogy.javalin.JavalinUtils.formParamOrEmpty;
import static com.agilogy.javalin.JavalinUtils.getFile;
import static com.agilogy.javalin.JavalinUtils.html;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import io.javalin.http.HttpStatus;
import static java.nio.charset.StandardCharsets.UTF_8;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.util.Map;

public final class TimeTrackingAppAdapter {

  private final @NotNull TimeTracking timeTracking;

  public TimeTrackingAppAdapter(@NotNull final TimeTracking timeTracking) {
    this.timeTracking = timeTracking;
  }

  public @NotNull EndpointGroup endpointGroup() {
    return () -> {
      get(ctx -> html(ctx, "index.html"));
      path("/uploadTimeEntries", () -> {
        get(ctx -> html(ctx, "uploadTimeEntriesForm.html"));
        post(ctx -> {
          final var employeeId = formParamOrEmpty(ctx, "employeeId");
          try {
            if (employeeId.isBlank()) throw new HttpException.ClientError("employeeId is required");
            final var timeEntries = getFile(ctx, "timeEntries", "text/csv")
                  .contentAndClose(is -> readTimeEntriesCsv(employeeId, new InputStreamReader(is, UTF_8)));
            timeTracking.addTimeEntries(timeEntries);
            ctx.redirect("listTimeEntries");
          } catch (HttpException.ClientError | TimeEntriesCsvException e) {
            final var status = e instanceof HttpException ? ((HttpException) e).status : 400;
            final var file = ctx.uploadedFile("timeEntries");
            final @NotNull var fileName = file != null ? file.filename() : "";
            html(
                  ctx,
                  status,
                  "uploadTimeEntriesForm.html",
                  Map.of("error", e.getMessage(), "fileName", fileName, "employeeId", employeeId)
            );
            ;
          }
        });
      });
      path("/listTimeEntries", () -> {
        get(ctx -> html(ctx, 200, "listTimeEntries.html", Map.of("timeEntries", timeTracking.listTimeEntries().stream().map(t -> t.roundToMinutes()).toList())));
      });
    };
  }

  public final @NotNull Map<Class<HttpException>, ExceptionHandler<HttpException>> handledExceptions =
        Map.of(HttpException.class, this::handleException);

  public void handleException(Exception exception, Context context) {
    switch (exception) {
      case HttpException e -> context.result(e.getMessage()).status(e.status);
      default -> context.result("Unexpected exception").status(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


}
