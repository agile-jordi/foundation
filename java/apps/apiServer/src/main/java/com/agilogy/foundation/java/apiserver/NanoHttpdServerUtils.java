package com.agilogy.foundation.java.apiserver;

import com.agilogy.foundation.java.apiserver.routing.Routing;
import com.agilogy.foundation.java.apiserver.routing.RoutingBuilder;
import com.agilogy.foundation.java.apiserver.routing.RoutingContext;
import fi.iki.elonen.NanoHTTPD;
import javautils.ThrowingRunnable;

import java.util.List;

public class NanoHttpdServerUtils {

  private static final Routing notFound =
        ctx -> NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "Not found");


  public static void start(final Routing routes, final int port, final boolean daemon) {
    var server = new NanoHTTPD(port) {
      @Override public Response serve(IHTTPSession session) {
        return RoutingBuilder.routes(routes, notFound).handle(new RoutingContext(List.of(), session));
      }
    };
    ThrowingRunnable.runOrThrow(() -> server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, daemon));
  }
}
