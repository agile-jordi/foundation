package com.agilogy.foundation.java.apiserver;

import com.agilogy.foundation.java.apiserver.routing.Routing;
import static com.agilogy.foundation.java.apiserver.routing.RoutingBuilder.get;
import static com.agilogy.foundation.java.apiserver.routing.RoutingBuilder.p;
import static com.agilogy.foundation.java.apiserver.routing.RoutingBuilder.post;
import static com.agilogy.foundation.java.apiserver.routing.RoutingBuilder.routes;
import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public final class TimeTrackingAdapter {

  private final Templates templates = new Templates();

  private Response redirect(final String uri) {
    final var result = newFixedLengthResponse(Response.Status.REDIRECT_SEE_OTHER, MIME_PLAINTEXT, "");
    result.addHeader("Location", uri);
    return result;
  }

  private final Routing routing = routes(
        get((r) -> redirect("/app")),
        p("app", routes(
                get((r) -> newFixedLengthResponse(templates.index())),
                p("uploadTimeEntries", routes(
                      get(r -> newFixedLengthResponse(templates.uploadTimeEntriesForm())),
                      post(r -> {
                        System.out.println("Uploading time entries");
                        return redirect("/");
                      })
                ))
          )
        )
  );

  public void start(final int port, final boolean daemon) {
    NanoHttpdServerUtils.start(routing, port, daemon);
  }

}
