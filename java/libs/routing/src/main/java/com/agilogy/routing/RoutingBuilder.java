package com.agilogy.routing;

import com.agilogy.routing.request.HttpRequest;
import com.agilogy.routing.request.StandarizedMethod;
import javautils.NotNull;
import javautils.Nullable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class RoutingBuilder {

  @SafeVarargs public static <A> @NotNull Routing<A> routes(
        @NotNull final Routing<A> routesHead,
        @NotNull final Routing<A>... routesTail
  ) {
    return request -> Stream.concat(Stream.of(routesHead), Arrays.stream(routesTail))
                            .map(r -> r.handle(request)).filter(Objects::nonNull).findFirst().orElse(null);
  }

  private static @NotNull HttpRequest removeFirstPathSegment(@NotNull HttpRequest request) {
    var path = request.originRequestTarget.path().stream().skip(1).reduce(
          "",
          (acc, segment) -> acc + "/" + URLEncoder.encode(segment, StandardCharsets.US_ASCII)
    );
    return new HttpRequest(request.method, path, request.headers);
  }

  private static @Nullable String firstPathSegment(@NotNull final HttpRequest request) {
    var originRequestTarget = request.originRequestTarget;
    if (originRequestTarget == null) return null;
    if (originRequestTarget.path().isEmpty()) return null;
    return originRequestTarget.path().get(0);
  }

  public static <A> @NotNull Routing<A> p(@NotNull final String segment, @NotNull final Routing<A> routes) {
    return request -> {
      var firstSegment = firstPathSegment(request);
      if (firstSegment != null && !firstSegment.equals(segment)) return null;
      HttpRequest requestToForward = removeFirstPathSegment(request);
      return routes.handle(requestToForward);
    };
  }

  public static <A> @NotNull Routing<A> p(
        @NotNull final Pattern pattern,
        @NotNull final Function<String, Routing<A>> route
  ) {
    return request -> {
      var firstSegment = firstPathSegment(request);
      if (firstSegment == null || !pattern.matcher(firstSegment).matches()) return null;
      return route.apply(firstSegment).handle(removeFirstPathSegment(request));

    };
  }

  private static final @NotNull Pattern intPattern = Pattern.compile("\\d+");

  public static <A> @NotNull Routing<A> p(@NotNull final Function<Integer, Routing<A>> route) {
    return p(intPattern, s -> route.apply(Integer.parseInt(s)));
  }

  public static <A> @NotNull Routing<A> handle(@NotNull final String method, final A result) {
    return request -> {
      boolean matched = request.method.equals(method) &&
                        request.originRequestTarget != null &&
                        request.originRequestTarget.path().isEmpty();
      if (!matched) return null;
      return result;
    };
  }

  public static <A> @NotNull Routing<A> handle(@NotNull final StandarizedMethod method, final A result) {
    return handle(method.name(), result);
  }

  public static <A> @NotNull Routing<A> get(final A result) {
    return handle(StandarizedMethod.GET, result);
  }

  public static <A> @NotNull Routing<A> post(final A result) {
    return handle(StandarizedMethod.POST, result);
  }

  public static <A> @NotNull Routing<A> put(final A result) {
    return handle(StandarizedMethod.PUT, result);
  }

  public static <A> @NotNull Routing<A> delete(final A result) {
    return handle(StandarizedMethod.DELETE, result);
  }
}
