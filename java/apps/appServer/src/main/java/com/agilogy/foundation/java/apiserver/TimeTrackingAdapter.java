package com.agilogy.foundation.java.apiserver;

import static com.agilogy.foundation.java.apiserver.NanoHttpdServerUtils.redirect;
import com.agilogy.foundation.java.domain.TimeTracking;
import com.agilogy.handlebars.HandlebarsTemplates;
import com.agilogy.routing.Routing;
import static com.agilogy.routing.RoutingBuilder.get;
import static com.agilogy.routing.RoutingBuilder.p;
import static com.agilogy.routing.RoutingBuilder.post;
import static com.agilogy.routing.RoutingBuilder.routes;
import fi.iki.elonen.NanoHTTPD;
import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import javautils.NotNull;
import static javautils.ThrowingRunnable.runUnchecked;
import static javautils.ThrowingSupplier.getUnchecked;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class TimeTrackingAdapter {

  private final @NotNull Routing<Function<NanoHTTPD.IHTTPSession, Response>> routing;

  public TimeTrackingAdapter(@NotNull final TimeTracking timeTracking) {
    final @NotNull var handlebarsTemplates = new HandlebarsTemplates();
    this.routing = routes(
          p("app", routes(
                  get(r -> newFixedLengthResponse(handlebarsTemplates.render("index.html"))),
                  p("uploadTimeEntries", routes(
                        get(r -> newFixedLengthResponse(handlebarsTemplates.render("uploadTimeEntriesForm.html"))),
                        post(r -> {
                          final Map<String, String> files = new HashMap<String, String>();
                          runUnchecked(() -> r.parseBody(files));
                          var timeEntries = getUnchecked(() -> Files.readString(Path.of(URI.create(files.get("timeEntries")))));
                          System.out.println(files);
                          System.out.println(timeEntries);
                          timeTracking.addTimeEntries();
                          System.out.println("Uploading time entries");
                          return redirect("/app");
                        })
                  ))
            )
          )
    );
  }


  public void start(final int port, final boolean daemon) {
    NanoHttpdServerUtils.start(routing, port, daemon);
  }

}
