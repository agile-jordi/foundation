package com.agilogy.foundation.java.apiserver;

import com.agilogy.routing.Routing;
import com.agilogy.routing.RoutingBuilder;
import com.agilogy.routing.request.HttpRequest;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import javautils.NotNull;
import static javautils.ThrowingRunnable.runUnchecked;

import java.util.function.Function;

public class NanoHttpdServerUtils {

  private static final @NotNull Routing<Function<IHTTPSession, Response>> notFound =
        r -> s -> NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not found");


  public static void start(
        @NotNull final Routing<Function<IHTTPSession, Response>> routes,
        final int port,
        final boolean daemon
  ) {
    var server = new NanoHTTPD(port) {
      @Override public Response serve(final IHTTPSession session) {
        var uri = session.getUri() == null ? "/" : session.getUri();
        var query = session.getQueryParameterString();
        var requestTarget = uri + (query == null ? "" : "?" + session.getQueryParameterString());
        // TODO: httpVersion
        var request = new HttpRequest(session.getMethod().name(), requestTarget, session.getHeaders());
        return RoutingBuilder.routes(routes, notFound).handle(request).apply(session);
      }
    };
    runUnchecked(() -> server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, daemon));
  }
}
