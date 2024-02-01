package com.agilogy.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import static javautils.ThrowingRunnable.runUnchecked;
import static javautils.ThrowingSupplier.getUnchecked;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class JsonCodec<A> implements JsonDecoder<A>, JsonEncoder<A> {

  private final @NotNull JsonEncoder<A> encoder;
  private final @NotNull JsonDecoder<A> decoder;

  private JsonCodec(@NotNull JsonEncoder<A> encoder, @NotNull JsonDecoder<A> decoder) {
    this.encoder = encoder;
    this.decoder = decoder;
  }

  public static <A> @NotNull JsonCodec<A> of(
        final @NotNull JsonEncoder<A> encoder,
        final @NotNull JsonDecoder<A> decoder
  ) {
    return new JsonCodec<>(encoder, decoder);
  }

  @Override public void encode(@NotNull JsonGenerator jsonGenerator, A value) {
    encoder.encode(jsonGenerator, value);
  }

  @Override public A decode(final @NotNull JsonParser parser) {
    return decoder.decode(parser);
  }

  public static @NotNull JsonCodec<Json> json = JsonCodec.of(JsonCodec::encodeJson, JsonCodec::decodeJson);

  public static @NotNull JsonCodec<Integer> intNum = JsonCodec.of(
        (g, i) -> runUnchecked(() -> g.writeNumber(i)),
        p -> getUnchecked(() -> p.getNumberValue().intValue())
  );
  public static @NotNull JsonCodec<Long> longNum = JsonCodec.of(
        (g, i) -> runUnchecked(() -> g.writeNumber(i)),
        p -> getUnchecked(() -> p.getNumberValue().longValue())
  );
  public static @NotNull JsonCodec<Double> doubleNum = JsonCodec.of(
        (g, d) -> runUnchecked(() -> g.writeNumber(d)),
        p -> getUnchecked(() -> p.getNumberValue().doubleValue())
  );
  public static @NotNull JsonCodec<String> string = JsonCodec.of(
        (g, s) -> runUnchecked(() -> g.writeString(s)),
        p -> getUnchecked(() -> p.getText())
  );
  public static @NotNull JsonCodec<Boolean> bool = JsonCodec.of(
        (g, b) -> runUnchecked(() -> g.writeBoolean(b)),
        p -> getUnchecked(p::getBooleanValue)
  );

  public static @NotNull JsonCodec<Instant> instantAsEpochMillis = JsonCodec.of(
        (g, i) -> runUnchecked(() -> g.writeNumber(i.toEpochMilli())),
        p -> getUnchecked(() -> Instant.ofEpochMilli(p.getNumberValue().longValue()))
  );

  static void encodeJson(final @NotNull JsonGenerator g, final @Nullable Json json) {
    runUnchecked(() -> {
      switch (json) {
        case null -> g.writeNull();
        case Json.Array array -> {
          g.writeStartArray();
          for (Json value : array) encodeJson(g, value);
          g.writeEndArray();
        }
        case Json.Boolean aBoolean -> g.writeBoolean(aBoolean.value());
        case Json.Number number -> g.writeNumber(number.value());
        case Json.Object object -> {
          g.writeStartObject();
          for (Map.Entry<String, Json> entry : object) {
            g.writeFieldName(entry.getKey());
            encodeJson(g, entry.getValue());
          }
          g.writeEndObject();
        }
        case Json.String s -> g.writeString(s.value());
      }
    });
  }

  static @Nullable Json decodeJson(final @NotNull JsonParser p) {
    return getUnchecked(() -> {
      int tokenId = p.hasCurrentToken() ? p.currentTokenId() : p.nextToken().id();
      switch (tokenId) {
        case JsonTokenId.ID_TRUE:
          return Json.Boolean.TRUE;
        case JsonTokenId.ID_FALSE:
          return Json.Boolean.FALSE;
        case JsonTokenId.ID_NUMBER_INT:
        case JsonTokenId.ID_NUMBER_FLOAT:
          return new Json.Number(p.getNumberValue().toString());
        case JsonTokenId.ID_STRING:
          return new Json.String(p.getText());
        case JsonTokenId.ID_START_ARRAY: {
          List<Json> values = new LinkedList<>();
          while (p.nextToken() != JsonToken.END_ARRAY) {
            values.add(decodeJson(p));
          }
          return new Json.Array(values);
        }
        case JsonTokenId.ID_START_OBJECT: {
          Map<String, Json> values = new HashMap<>();
          while (p.nextToken() != JsonToken.END_OBJECT) {
            final String currentName = p.getCurrentName();
            p.nextToken();
            values.put(currentName, decodeJson(p));
          }
          return new Json.Object(values);
        }
        case JsonTokenId.ID_NULL:
          return null;
        default:
      }
      throw new UnsupportedOperationException("Unsupported token id " + tokenId + " (" + p.currentToken() + ")");
    });
  }
}
