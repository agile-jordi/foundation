package com.agilogy.routing.request;

import javautils.NotNull;
import javautils.Nullable;

import java.util.Map;
import java.util.Objects;

/** See <a href="https://www.rfc-editor.org/rfc/rfc7230.html#section-3">rfc7230</a> */
public final class HttpRequest {
  /**
   * - See <a href="https://www.rfc-editor.org/rfc/rfc7230.html#section-3.1.1">rfc7230 section 3.1.1</a> - See <a
   * href="https://www.rfc-editor.org/rfc/rfc7231#section-4">rfc7231 section 4</a> for standarized methods. - See <a
   * href="https://www.iana.org/assignments/http-methods/http-methods.xhtml">IANA http-methods</a> for non standarized
   * methods.
   */
  public final @NotNull String method;

  /**
   * - See <a href="https://www.rfc-editor.org/rfc/rfc7230.html#section-3.1.1">rfc7230 section 3.1.1</a> - See <a
   * href="https://www.rfc-editor.org/rfc/rfc7230.html#section-5.3">rfc7230 section 5.3</a>
   */
  public final @NotNull String requestTarget;

  /**
   * - See <a href="https://www.rfc-editor.org/rfc/rfc7230.html#section-5.3.1">rfc7230 section 5.3.1</a>
   */
  public final @Nullable OriginFormRequestTarget originRequestTarget;
  public final @NotNull Map<String, String> headers;

  /** The standarized method if the method is standarized, null otherwise. */
  public final @Nullable StandarizedMethod standarizedMethod;

  public HttpRequest(
        @NotNull final String method,
        @NotNull final String requestTarget,
        @NotNull final Map<String, String> headers
  ) {
    this.method = method;
    this.requestTarget = requestTarget.isEmpty() ? "/" : requestTarget;
    this.originRequestTarget = OriginFormRequestTarget.of(requestTarget);
    this.headers = headers;
    this.standarizedMethod = StandarizedMethod.of(method);
  }

  public @NotNull String asString() {
    return method + " " + requestTarget + "\n" + headers.entrySet()
                                                        .stream()
                                                        .map(e -> e.getKey() + ": " + e.getValue())
                                                        .reduce("", (acc, h) -> acc + "\n" + h);
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HttpRequest that = (HttpRequest) o;
    return Objects.equals(method, that.method) &&
           Objects.equals(requestTarget, that.requestTarget) &&
           Objects.equals(headers, that.headers);
  }

  @Override public int hashCode() {
    return Objects.hash(method, requestTarget, headers);
  }
}
