package com.agilogy.routing.request;

import javautils.NotNull;
import javautils.Nullable;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

public record OriginFormRequestTarget(@NotNull List<String> path, @Nullable String rawQuery) {
  static @Nullable OriginFormRequestTarget of(@NotNull final String originForm) {
    URI uri;
    try {
      uri = URI.create(originForm);
    } catch (IllegalArgumentException e) {
      uri = null;
    }
    if (uri == null || uri.isAbsolute() || uri.getRawAuthority() != null) return null;
    var sPath = uri.getPath();
    var path = sPath == null ? List.<String>of() : Stream.of(sPath.split("/")).filter(s -> !s.isEmpty()).toList();
    return new OriginFormRequestTarget(path, uri.getRawQuery());
  }

}
