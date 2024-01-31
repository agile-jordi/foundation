package com.agilogy.json;

import static com.agilogy.json.Json.json;
import static com.agilogy.json.Json.jsonArr;
import static com.agilogy.json.Json.jsonObj;
import static java.util.Map.entry;
import org.jetbrains.annotations.NotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class JsonCodecTest {

  public void testParseAndSerialize(final @NotNull Json value) {
    var str = value.asString();
    var json = JsonCodec.parse(str);
    assertEquals(value, json);
  }

  public void testParseAndSerialize(final @NotNull Json value, final @NotNull String expected) {
    var str = value.asString();
    assertEquals(expected, str);
    var json = JsonCodec.parse(str);
    assertEquals(value, json);
  }

  @Test
  public void parseAndSerializeStrings() {
    testParseAndSerialize(json("Hello World!"), "\"Hello World!\"");
  }

  @Test
  public void parseAndSerializeBooleans() {
    testParseAndSerialize(json(true), "true");
    testParseAndSerialize(json(false), "false");
  }

  @Test
  public void parseAndSerializeNumbers() {
    testParseAndSerialize(json(1), "1");
    testParseAndSerialize(json(2L), "2");
    testParseAndSerialize(json(3.0), "3.0");
  }

  @Test
  public void parseAndSerializeTheEmptyArray() {
    testParseAndSerialize(jsonArr(), "[]");
  }

  @Test
  public void parseAndSerializeArray() {
    testParseAndSerialize(jsonArr(json(1), json("Hi"), jsonObj(entry("a", json("a")))));
  }

  @Test
  public void parseAndSerializeTheEmptyObject() {
    testParseAndSerialize(jsonObj(), "{}");
  }

  @Test
  public void parseAndSerializeObjectWithNumberAttributes() {
    testParseAndSerialize(jsonObj(entry("a", json(1))), "{\"a\":1}");
  }

  @Test
  public void parseAndSerializeObjectWithAttributes() {
    testParseAndSerialize(jsonObj(
          entry("a", json(1)),
          entry("b", json(true)),
          entry("c", jsonArr(json(1), json(2)))
    ));
  }
}
