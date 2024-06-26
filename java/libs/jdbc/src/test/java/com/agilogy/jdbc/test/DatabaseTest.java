package com.agilogy.jdbc.test;

import static com.agilogy.jdbc.Param.p;
import com.agilogy.jdbc.RowReader;
import com.agilogy.jdbc.test.utils.DatabaseBackedTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

public final class DatabaseTest extends DatabaseBackedTest {

  @BeforeEach
  public void setUpDatabaseSchema() {
    db.update("create table if not exists users(id serial primary key, name text, email text)");
    db.update("delete from users");
  }

  private final RowReader<String> readNameAndEmail = (rs) -> rs.getString(1) + " <" + rs.getString(2) + ">";

  @Test
  public void testInsertAndSelect() {
    db.update("insert into users(name, email) values(?,?)", p("John"), p("john@example.com"));
    final var actual = db.select("select name, email from users where name = ?", readNameAndEmail, p("John"));
    assertEquals(List.of("John <john@example.com>"), actual);
  }

  @Test
  public void testInsertAndGetKey() {
    final var key = db.insertAndGetKey(
          "insert into users(name, email) values(?,?)",
          (rs) -> rs.getInt(1),
          p("John"), p("john@example.com")
    );
    final var actual = db.selectOne("select name, email from users where id = ?", readNameAndEmail, p(key));
    assertEquals("John <john@example.com>", actual);
  }

  @Test
  public void testInTransactionAbortsWhenATransactionIsThrown() {
    assertThrows(RuntimeException.class, () -> db.inTransaction((conn) -> {
      conn.update("insert into users(name, email) values(?,?)", p("John"), p("Doe"));
      conn.update("insert into users(name, email) values(?,?)", p("Jane"), p("Doe"));
      throw new RuntimeException("Rollback");
    }));
    final var finalRows = db.selectOne("select count(*) from users", (rs) -> rs.getInt(1));
    assertEquals(0, finalRows);
  }

  @Test
  public void testWithConnectionDoesNotAbortsWhenATransactionIsThrown() {
    assertThrows(RuntimeException.class, () -> db.withConnection((conn) -> {
      conn.update("insert into users(name, email) values(?,?)", p("John"), p("Doe"));
      conn.update("insert into users(name, email) values(?,?)", p("Jane"), p("Doe"));
      throw new RuntimeException("Rollback");
    }));
    final var finalRows = db.selectOne("select count(*) from users", (rs) -> rs.getInt(1));
    assertEquals(2, finalRows);
  }

  @Test
  public void testSelect() {
    assertEquals(List.of("Hey!", "Ho!"), db.select("Select 'Hey!' union all select 'Ho!'", (rs) -> rs.getString(1)));
    assertEquals(List.of(), db.select("Select 'Hey!' where false", (rs) -> rs.getString(1)));
  }

  @Test
  public void testSelectToSet() {
    assertEquals(
          Set.of("Hey!", "Ho!"),
          db.selectToSet("select 'Hey!' union all select 'Ho!' union all select 'Hey!'", (rs) -> rs.getString(1))
    );
    assertEquals(Set.of(), db.selectToSet("Select 'Hey!' where false", (rs) -> rs.getString(1)));
  }

  @Test
  public void testSelectOne() {
    assertEquals("Hi", db.selectOne("select 'Hi'", (rs) -> rs.getString(1)));
  }

  @Test
  public void testSelectOneReturnsThrowsWhenThereAreNoResults() {
    final var exception =
          assertThrows(RuntimeException.class, () -> db.selectOne("select 'Hi' where false", (rs) -> rs.getString(1)));
    assertEquals("java.sql.SQLException: No rows returned", exception.getMessage());

  }

  @Test
  public void testSelectOneThrowsWhenMoreThanOneRowReturned() {
    final var exception = assertThrows(
          RuntimeException.class,
          () -> db.selectOne("select 'Hello' union all select 'World'", (rs) -> rs.getString(1))
    );
    assertEquals("java.sql.SQLException: More than one row returned", exception.getMessage());
  }


  @Test
  public void testStringParameterAndResult() {
    assertEquals("Hi", db.selectOne("select ?", (rs) -> rs.getString(1), p("Hi")));
    assertEquals((String) null, db.selectOne("select ?", (rs) -> rs.getString(1), p((String) null)));
  }

  @Test
  public void testBooleanParameterAndResult() {
    assertEquals(true, db.selectOne("select ?", (rs) -> rs.getBool(1), p(true)));
    final var exception =
          assertThrows(RuntimeException.class, () -> db.selectOne("select null", (rs) -> rs.getBool(1)));
    assertEquals("java.sql.SQLException: Column 1 is null", exception.getMessage());
    assertEquals(Boolean.TRUE, db.selectOne("select ?", (rs) -> rs.getBoolean(1), p(Boolean.TRUE)));
    assertEquals((Boolean) null, db.selectOne("select ?", (rs) -> rs.getBoolean(1), p((Boolean) null)));

  }

  @Test
  public void testFloatParameterAndResult() {
    assertEquals((Float) 23.0f, db.selectOne("select ?", (rs) -> rs.getFloatPrimitive(1), p(23.0f)));
    final var exception =
          assertThrows(RuntimeException.class, () -> db.selectOne("select null", (rs) -> rs.getFloatPrimitive(1)));
    assertEquals("java.sql.SQLException: Column 1 is null", exception.getMessage());
    assertEquals(Float.valueOf(23.0f), db.selectOne("select ?", (rs) -> rs.getFloat(1), p(Float.valueOf(23.0f))));
    assertEquals((Float) null, db.selectOne("select ?", (rs) -> rs.getFloat(1), p((Float) null)));
  }

  @Test
  public void testDoubleParameterAndResult() {
    assertEquals(23.0, db.selectOne("select ?", (rs) -> rs.getDoublePrimitive(1), p(23.0)));
    final var exception =
          assertThrows(RuntimeException.class, () -> db.selectOne("select null", (rs) -> rs.getDoublePrimitive(1)));
    assertEquals("java.sql.SQLException: Column 1 is null", exception.getMessage());
    assertEquals(Double.valueOf(23.0), db.selectOne("select ?", (rs) -> rs.getDouble(1), p(Double.valueOf(23.0))));
    assertEquals((Double) null, db.selectOne("select ?", (rs) -> rs.getDouble(1), p((Double) null)));
  }

  @Test
  public void testIntParameterAndResult() {
    assertEquals((Integer) 23, db.selectOne("select ?", (rs) -> rs.getInt(1), p(23)));
    final var exception =
          assertThrows(RuntimeException.class, () -> db.selectOne("select null", (rs) -> rs.getInt(1)));
    assertEquals("java.sql.SQLException: Column 1 is null", exception.getMessage());
    assertEquals(Integer.valueOf(23), db.selectOne("select ?", (rs) -> rs.getInteger(1), p(Integer.valueOf(23))));
    assertEquals((Integer) null, db.selectOne("select ?", (rs) -> rs.getInteger(1), p((Integer) null)));
  }

  @Test
  public void testLongParameterAndResult() {
    assertEquals((Long) 23L, db.selectOne("select ?", (rs) -> rs.getLongPrimitive(1), p(23L)));
    final var exception =
          assertThrows(RuntimeException.class, () -> db.selectOne("select null", (rs) -> rs.getLongPrimitive(1)));
    assertEquals("java.sql.SQLException: Column 1 is null", exception.getMessage());
    assertEquals(Long.valueOf(23), db.selectOne("select ?", (rs) -> rs.getLong(1), p(Long.valueOf(23))));
    assertEquals((Long) null, db.selectOne("select ?", (rs) -> rs.getLong(1), p((Long) null)));
  }

  @Test
  public void testInstantParameterAndResult() {
    final var instant = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    assertEquals(instant, db.selectOne("select ?", (rs) -> rs.getInstant(1), p(instant)));
    assertEquals((Instant) null, db.selectOne("select ?", (rs) -> rs.getInstant(1), p((Instant) null)));
  }

  @Test
  public void testUriParameterAndResult() {
    final var uri = URI.create("http://example.com");
    assertEquals(uri, db.selectOne("select ?", (rs) -> rs.getUri(1), p(uri)));
    assertEquals((URI) null, db.selectOne("select ?", (rs) -> rs.getUri(1), p((URI) null)));
  }

}
