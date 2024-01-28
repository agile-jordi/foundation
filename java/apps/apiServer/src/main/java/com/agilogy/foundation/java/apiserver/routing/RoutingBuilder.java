package com.agilogy.foundation.java.apiserver.routing;

import static com.agilogy.foundation.java.apiserver.routing.RoutingBuilder.get;
import static com.agilogy.foundation.java.apiserver.routing.RoutingBuilder.p;
import static com.agilogy.foundation.java.apiserver.routing.RoutingBuilder.post;
import static com.agilogy.foundation.java.apiserver.routing.RoutingBuilder.put;
import static com.agilogy.foundation.java.apiserver.routing.RoutingBuilder.routes;
import fi.iki.elonen.NanoHTTPD;

import java.util.function.Function;
import java.util.regex.Pattern;

public class RoutingBuilder {

  public static Routing routes(Routing... routings) {
    return ctx -> {
      for (Routing routing : routings) {
        NanoHTTPD.Response response = routing.handle(ctx);
        if (response != null) {
          return response;
        }
      }
      return null;
    };
  }

  public static Routing p(String segment, Routing routing) {
    return ctx -> {
      if (!ctx.startsWith(segment)) return null;
      return routing.handle(ctx.withRouted(segment));

    };
  }

  public static Routing p(Pattern pattern, Function<String, Routing> route) {
    return ctx -> {
      var firstSegment = ctx.pendingPath.isEmpty() ? null : ctx.pendingPath.get(0);
      if (firstSegment == null || !pattern.matcher(firstSegment).matches()) return null;
      return route.apply(firstSegment).handle(ctx.withRouted(firstSegment));

    };
  }

  private static Pattern intPattern = Pattern.compile("\\d+");

  public static Routing p(Function<Integer, Routing> route) {
    return p(intPattern, s -> route.apply(Integer.parseInt(s)));
  }

  public static Routing handle(NanoHTTPD.Method method, Function<NanoHTTPD.IHTTPSession, NanoHTTPD.Response> handler) {
    return ctx -> {
      boolean matched = ctx.request.getMethod() == method && ctx.pendingPath.isEmpty();
      if (!matched) return null;
      return handler.apply(ctx.request);
    };
  }


  public static Routing get(Function<NanoHTTPD.IHTTPSession, NanoHTTPD.Response> handler) {
    return handle(NanoHTTPD.Method.GET, handler);
  }

  public static Routing post(Function<NanoHTTPD.IHTTPSession, NanoHTTPD.Response> handler) {
    return handle(NanoHTTPD.Method.POST, handler);
  }

  public static Routing put(Function<NanoHTTPD.IHTTPSession, NanoHTTPD.Response> handler) {
    return handle(NanoHTTPD.Method.PUT, handler);
  }
}

class Test {

  private Routing example() {
    return routes(
          p("users", routes(
                  p((Integer id) -> routes(
                        get((req) -> NanoHTTPD.newFixedLengthResponse("GET /users/" + id)),
                        put((req) -> NanoHTTPD.newFixedLengthResponse("PUT /users/" + id))
                  )),
                  post((req) -> NanoHTTPD.newFixedLengthResponse("POST /users"))
            )
          )
    );
  }
}