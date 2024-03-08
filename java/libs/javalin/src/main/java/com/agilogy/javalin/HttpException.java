package com.agilogy.javalin;

import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class HttpException extends RuntimeException {

  public final int status;

  public HttpException(final int status, final @NotNull String message) {
    super(message);
    this.status = status;
  }

  public HttpException(final @NotNull HttpStatus status, final @NotNull String message) {
    this(status.getCode(), message);
  }

  public static class ClientError extends HttpException {
    public ClientError(final @NotNull HttpStatus status, final @NotNull String message) {
      super(status, message);
      if (status.getCode() < 400 || status.getCode() >= 500)
        throw new IllegalArgumentException("ClientError status code must be in the 4xx range");
    }

    public ClientError(final @NotNull String message) {
      super(HttpStatus.BAD_REQUEST, message);
    }
  }

  public static class ValidationErrors extends HttpException.ClientError {

    public final Map<String, String> errors;
    public ValidationErrors(Map<String, String> errors) {
      super("Validation errors: " + errors.keySet());
      this.errors = errors;
    }

  }
}


