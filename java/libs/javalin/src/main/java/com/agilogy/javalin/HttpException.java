package com.agilogy.javalin;

import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

public abstract class HttpException extends RuntimeException {

  public final int status;

  public HttpException(final int status, final @NotNull String message) {
    super(message);
    this.status = status;
  }

  public HttpException(final @NotNull HttpStatus status, final @NotNull String message) {
    this(status.getCode(), message);
  }

  public static final class ClientError extends HttpException {
    public ClientError(final @NotNull HttpStatus status, final @NotNull String message) {
      super(status, message);
      if (status.getCode() < 400 || status.getCode() >= 500)
        throw new IllegalArgumentException("ClientError status code must be in the 4xx range");
    }

    public ClientError(final @NotNull String message) {
      super(HttpStatus.BAD_REQUEST, message);

    }
  }
}


