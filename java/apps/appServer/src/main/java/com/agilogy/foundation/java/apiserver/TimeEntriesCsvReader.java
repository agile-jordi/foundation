package com.agilogy.foundation.java.apiserver;

import static com.agilogy.foundation.java.apiserver.TimeEntriesCsvReader.TimeEntriesCsvHeaders.end;
import static com.agilogy.foundation.java.apiserver.TimeEntriesCsvReader.TimeEntriesCsvHeaders.project;
import static com.agilogy.foundation.java.apiserver.TimeEntriesCsvReader.TimeEntriesCsvHeaders.start;
import com.agilogy.foundation.java.domain.TimeEntry;
import static javautils.ThrowingSupplier.getUnchecked;
import static javautils.collections.CollectionsUtils.diff;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class TimeEntriesCsvReader {

  private TimeEntriesCsvReader() {
    throw new UnsupportedOperationException("This class cannot be instantiated");
  }

  enum TimeEntriesCsvHeaders {
    project("project"), start("start"), end("end");

    public final @NotNull String label;

    TimeEntriesCsvHeaders(final @NotNull String label) {
      this.label = label;
    }

    final static @NotNull Set<String> names =
          Arrays.stream(TimeEntriesCsvHeaders.values()).map(h -> h.label).collect(Collectors.toSet());
  }

  public static @NotNull List<TimeEntry> readTimeEntriesCsv(
        final @NotNull String employeeId,
        final @NotNull Reader reader
  ) {
    return getUnchecked(() -> {
      try (reader) {
        final var parser = CSVFormat.DEFAULT.builder().setHeader().build().parse(reader);
        final var headerNames = new HashSet<>(parser.getHeaderNames());
        final var missingHeaderNames = diff(TimeEntriesCsvHeaders.names, headerNames);
        if (!missingHeaderNames.isEmpty()) {
          throw new TimeEntriesCsvException.MissingHeaders(missingHeaderNames);
        }
        return parser.stream().map(te -> TimeEntriesCsvReader.readRow(employeeId, te)).toList();
      }
    });
  }

  private static @NotNull TimeEntry readRow(final @NotNull String employeeId, final @NotNull CSVRecord csvRecord) {
    return new TimeEntry(
          employeeId,
          csvRecord.get(project.label),
          parseInstant(csvRecord.get(start.label)),
          parseInstant(csvRecord.get(end.label))
    );
  }

  private static @NotNull Instant parseInstant(final @NotNull String s) {
    try {
      return Instant.parse(s);
    } catch (DateTimeParseException e) {
      return Instant.ofEpochSecond(Long.parseLong(s));
    }
  }
}
