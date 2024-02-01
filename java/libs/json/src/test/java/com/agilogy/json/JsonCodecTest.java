package com.agilogy.json;

import static com.agilogy.json.Json.json;
import static com.agilogy.json.Json.jsonArr;
import static com.agilogy.json.Json.jsonObj;
import static com.agilogy.json.JsonCodecTestHelper.testCodec;
import static java.util.Map.entry;
import org.junit.jupiter.api.Test;

public class JsonCodecTest {

  @Test
  public void parseAndSerializeStrings() {
    testCodec(json("Hello World!"), "\"Hello World!\"");
  }

  @Test
  public void parseAndSerializeBooleans() {
    testCodec(json(true), "true");
    testCodec(json(false), "false");
  }

  @Test
  public void parseAndSerializeNumbers() {
    testCodec(json(1), "1");
    testCodec(json(2L), "2");
    testCodec(json(3.0), "3.0");
  }

  @Test
  public void parseAndSerializeTheEmptyArray() {
    testCodec(jsonArr(), "[]");
  }

  @Test
  public void parseAndSerializeArray() {
    testCodec(jsonArr(json(1), json("Hi"), jsonObj(entry("a", json("a")))));
  }

  @Test
  public void parseAndSerializeTheEmptyObject() {
    testCodec(jsonObj(), "{}");
  }

  @Test
  public void parseAndSerializeObjectWithNumberAttributes() {
    testCodec(jsonObj(entry("a", json(1))), "{\"a\":1}");
  }

  @Test
  public void parseAndSerializeObjectWithAttributes() {
    testCodec(jsonObj(
          entry("a", json(1)),
          entry("b", json(true)),
          entry("c", jsonArr(json(1), json(2)))
    ));
  }
}
