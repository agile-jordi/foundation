package com.agilogy.foundation.java.apiserver;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class TimeEntriesCsvException extends RuntimeException {

  public TimeEntriesCsvException(String message) {
    super(message);
  }

  public static final class MissingHeaders extends TimeEntriesCsvException {
    public final @NotNull Set<String> missingHeaders;

    public MissingHeaders(final @NotNull Set<String> missingHeaders) {
      super("Missing headers: " + String.join(", ", missingHeaders));
      this.missingHeaders = missingHeaders;
    }
  }
}






