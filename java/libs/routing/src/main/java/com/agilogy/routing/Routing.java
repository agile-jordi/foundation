package com.agilogy.routing;

import com.agilogy.routing.request.HttpRequest;
import javautils.NotNull;
import javautils.Nullable;

@FunctionalInterface
public interface Routing<A> {
  @Nullable A handle(@NotNull final HttpRequest request);
}