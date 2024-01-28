package com.agilogy.foundation.java.apiserver.routing;

import fi.iki.elonen.NanoHTTPD;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RoutingContext {
  public final List<String> routedPath;
  public final NanoHTTPD.IHTTPSession request;
  public final URI uri;
  public final List<String> fullPath;

  public final List<String> pendingPath;

  public RoutingContext(List<String> routedPath, NanoHTTPD.IHTTPSession request) {
    this.routedPath = routedPath;
    this.request = request;
    this.uri = URI.create(request.getUri());
    this.fullPath = uri.getPath().isEmpty() ? List.of() : Arrays.stream(uri.getPath().split("/")).filter(s -> !s.isEmpty()).toList();
    this.pendingPath = fullPath.subList(routedPath.size(), fullPath.size());
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RoutingContext that = (RoutingContext) o;
    return Objects.equals(routedPath, that.routedPath) && Objects.equals(request, that.request);
  }

  @Override public int hashCode() {
    return Objects.hash(routedPath, request);
  }

  public RoutingContext withRouted(String segment) {
    if(pendingPath.isEmpty() || !pendingPath.get(0).equals(segment)) {
      throw new IllegalArgumentException("Cannot route to " + routedPath + " because the pending path is " + pendingPath);
    }
    var newRoutedPath = new ArrayList<>(routedPath);
    newRoutedPath.add(segment);
    return new RoutingContext(newRoutedPath , request);
  }

  public boolean startsWith(String segment) {
    return !pendingPath.isEmpty() && pendingPath.get(0).equals(segment);
  }
}
