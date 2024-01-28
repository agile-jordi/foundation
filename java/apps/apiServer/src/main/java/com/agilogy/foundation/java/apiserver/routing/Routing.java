package com.agilogy.foundation.java.apiserver.routing;

import fi.iki.elonen.NanoHTTPD.Response;

@FunctionalInterface
public interface Routing {
  Response handle(RoutingContext ctx);
}