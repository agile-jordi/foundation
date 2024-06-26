package com.agilogy.jdbc.test.utils;

import com.agilogy.jdbc.Database;
import static com.agilogy.jdbc.HikariCP.getDataSource;
import static com.agilogy.jdbc.test.utils.ConsoleUtils.printAlignedTable;
import javautils.ThrowingFunction;
import javautils.ThrowingSupplier;
import javautils.ThrowingConsumer;
import static javautils.collections.CollectionsUtils.join;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseBackedTest {

  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:15-alpine"
  );

  protected static Database db;


  @BeforeAll
  static void beforeAll() {
    postgres.start();
    db = new Database(getDataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));

  }

  @AfterAll
  static void afterAll() {
    bracket(() -> db, Database::close, (db) -> postgres.stop());
  }


  private static <R> void bracket(
        ThrowingSupplier<@NotNull R> acquire,
        ThrowingConsumer<@NotNull R> use,
        ThrowingConsumer<@NotNull R> release
  ){

  }
  private static <R, A> A bracket(
        ThrowingSupplier<@NotNull R> acquire,
        ThrowingFunction<@NotNull R, A> use,
        ThrowingConsumer<@NotNull R> release
  ) {
    final R resource = acquire.get();
    try {
      return use.apply(resource);
    } catch (Throwable e) {
      try {
        release.accept(resource);
      } catch (Throwable e2) {
        e2.addSuppressed(e);
        throw unchecked(e2);
      }
      throw unchecked(e);
    }
  }

  private static RuntimeException unchecked(Throwable e) {
    if (e instanceof RuntimeException) return (RuntimeException) e;
    return new RuntimeException(e);
  }


  @AfterEach
  public final void afterEach() {
    printDbContents();
  }


  private void printDbContents() {
    final var tables = db.select(
          "SELECT table_name, table_schema FROM information_schema.tables where table_schema = 'public'",
          (rs) -> rs.getString(1)
    );
    tables.forEach(this::printTableContents);
  }

  private void printTableContents(final String tableName) {
    final var columns = db.select(
          "SELECT column_name FROM information_schema.columns WHERE table_name = '" + tableName + "'",
          (rs) ->
                rs.getString(1)
    );
    final var columnNames = String.join(", ", columns);
    final var rows = db.select("SELECT " + columnNames + " FROM " + tableName, (rs) -> {
      final List<String> row = new ArrayList<>();
      for (int i = 1; i <= columns.size(); i++) {
        row.add(rs.getString(i));
      }
      return row;
    });
    System.out.println(("-- " + tableName + " " + "-".repeat(75)).substring(0, 80) + "\n");
    printAlignedTable(join(List.of(columns), rows));
    System.out.println();
  }

}
