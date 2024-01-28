package com.agilogy.routing.request;

import javautils.NotNull;
import javautils.Nullable;

/** See */
public enum StandarizedMethod {
  GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE;

  static @Nullable StandarizedMethod of(@NotNull final String s) {
    return switch (s) {
      case "GET" -> GET;
      case "HEAD" -> HEAD;
      case "POST" -> POST;
      case "PUT" -> PUT;
      case "DELETE" -> DELETE;
      case "CONNECT" -> CONNECT;
      case "OPTIONS" -> OPTIONS;
      case "TRACE" -> TRACE;
      default -> null;
    };
  }
}
