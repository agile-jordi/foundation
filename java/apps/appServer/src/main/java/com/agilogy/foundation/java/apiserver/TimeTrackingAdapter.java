package com.agilogy.foundation.java.apiserver;

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

import java.util.function.Function;

public final class TimeTrackingAdapter {

  private final @NotNull HandlebarsTemplates handlebarsTemplates = new HandlebarsTemplates();

  private @NotNull Response redirect(@NotNull final String uri) {
    final var result = newFixedLengthResponse(Response.Status.REDIRECT_SEE_OTHER, MIME_PLAINTEXT, "");
    result.addHeader("Location", uri);
    return result;
  }

  private final @NotNull Routing<Function<NanoHTTPD.IHTTPSession, Response>> routing = routes(
        p("app", routes(
                get(r -> newFixedLengthResponse(handlebarsTemplates.render("index.html"))),
                p("uploadTimeEntries", routes(
                      get(r -> newFixedLengthResponse(handlebarsTemplates.render("uploadTimeEntriesForm.html"))),
                      post(r -> {
                        System.out.println("Uploading time entries");
                        return redirect("/app");
                      })
                ))
          )
        )
  );

  public void start(final int port, final boolean daemon) {
    NanoHttpdServerUtils.start(routing, port, daemon);
  }

}
