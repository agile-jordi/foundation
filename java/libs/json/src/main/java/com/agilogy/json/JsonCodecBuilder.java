package com.agilogy.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import static javautils.ThrowingRunnable.runUnchecked;
import static javautils.ThrowingSupplier.getUnchecked;
import javautils.function.Function3;
import javautils.function.Function4;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public final class JsonCodecBuilder {

  public static <A1, B> @NotNull JsonDecoder<B> objectCodec(
        final @NotNull Function<A1, B> constructor,
        final @NotNull Field<B, A1> field1
  ) {
    final JsonDecoder<B> decoder = p -> {
      List<Field<B, ?>> fields = List.of(field1);
      List<Object> values = getObjectFields(fields, p);
      return constructor.apply((A1) values.getFirst());
    };
    final JsonEncoder<B> encoder =
          (p, value) -> runUnchecked(() -> writeObject(p, value, field1));
    return JsonCodec.of(encoder, decoder);
  }

  public static <A1, A2, B> @NotNull JsonDecoder<B> objectCodec(
        final @NotNull BiFunction<A1, A2, B> constructor,
        final @NotNull Field<B, A1> field1,
        final @NotNull Field<B, A2> field2
  ) {
    final JsonDecoder<B> decoder = p -> {
      var fields = List.of(field1, field2);
      List<Object> values = getObjectFields(fields, p);
      return constructor.apply((A1) values.get(0), (A2) values.get(1));
    };
    final JsonEncoder<B> encoder =
          (p, value) -> runUnchecked(() -> writeObject(p, value, field1, field2));
    return JsonCodec.of(encoder, decoder);
  }

  public static <A1, A2, A3, B> JsonDecoder<B> objectCodec(
        final @NotNull Function3<A1, A2, A3, B> constructor,
        final @NotNull Field<B, A1> field1,
        final @NotNull Field<B, A2> field2,
        final @NotNull Field<B, A3> field3
  ) {
    final JsonDecoder<B> decoder = p -> {
      var fields = List.of(field1, field2, field3);
      List<Object> values = getObjectFields(fields, p);
      return constructor.apply((A1) values.get(0), (A2) values.get(1), (A3) values.get(2));
    };
    final JsonEncoder<B> encoder =
          (p, value) -> runUnchecked(() -> writeObject(p, value, field1, field2, field3));
    return JsonCodec.of(encoder, decoder);
  }

  public static <A1, A2, A3, A4, B> JsonCodec<B> objectCodec(
        final @NotNull Function4<A1, A2, A3, A4, B> constructor,
        final @NotNull Field<B, A1> field1,
        final @NotNull Field<B, A2> field2,
        final @NotNull Field<B, A3> field3,
        final @NotNull Field<B, A4> field4
  ) {
    final JsonDecoder<B> decoder = p -> {
      var fields = List.of(field1, field2, field3, field4);
      List<Object> values = getObjectFields(fields, p);
      return constructor.apply((A1) values.get(0), (A2) values.get(1), (A3) values.get(2), (A4) values.get(3));
    };
    final JsonEncoder<B> encoder =
          (p, value) -> runUnchecked(() -> writeObject(p, value, field1, field2, field3, field4));
    return JsonCodec.of(encoder, decoder);
  }

  @SafeVarargs private static <A> void writeObject(
        final @NotNull JsonGenerator p,
        final @NotNull A value,
        final @NotNull Field<A, ?>... fields
  ) {
    runUnchecked(() -> {
      p.writeStartObject();
      for (final var field : fields) writeField(p, field, value);
      p.writeEndObject();
    });
  }

  private static <A, B> void writeField(final @NotNull JsonGenerator p, final @NotNull Field<A, B> field, A value) {
    runUnchecked(() -> {
      p.writeFieldName(field.name());
      field.codec().encode(p, field.get(value));
    });
  }

  private static <A> List<Object> getObjectFields(
        final @NotNull List<Field<A, ?>> fields,
        final @NotNull JsonParser p
  ) {
    return getUnchecked(() -> {
      var fieldMap = fields.stream().collect(Collectors.toMap(Field::name, Function.identity()));
      int tokenId = p.hasCurrentToken() ? p.currentTokenId() : p.nextToken().id();
      switch (tokenId) {
        case JsonTokenId.ID_START_OBJECT: {
          List<Object> values = new ArrayList<>(1);
          while (p.nextToken() != JsonToken.END_OBJECT) {
            final String currentName = p.getCurrentName();
            p.nextToken();
            values.add(fieldMap.get(currentName).codec().decode(p));
          }
          return values;
        }
        case JsonTokenId.ID_NULL:
          return null;
        default:
      }
      throw new UnsupportedOperationException("Unsupported token id " + tokenId + " (" + p.currentToken() + ")");
    });
  }

}
