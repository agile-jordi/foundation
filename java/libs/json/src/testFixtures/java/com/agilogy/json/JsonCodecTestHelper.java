package com.agilogy.json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class JsonCodecTestHelper {


  public static <A> void testCodec(
        final @NotNull A value,
        final @NotNull JsonCodec<A> codec,
        final @Nullable String expected
  ) {
    var json = codec.encode(value);
    if (expected != null) {
      assertEquals(expected, json);
    }
    var decoded = codec.decode(json);
    assertEquals(value, decoded);
  }

  public static <A> void testCodec(final @NotNull A value, final @NotNull JsonCodec<A> codec) {
    testCodec(value, codec, null);
  }

  public static void testCodec(final @NotNull Json value) {
    testCodec(value, JsonCodec.json);
  }

  public static void testCodec(final @NotNull Json value, final @NotNull String expected) {
    testCodec(value, JsonCodec.json, expected);
  }
}
