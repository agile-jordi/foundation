package com.agilogy.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import static javautils.ThrowingSupplier.getUnchecked;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

@FunctionalInterface
public interface JsonEncoder<A> {
  void encode(final @NotNull JsonGenerator jsonGenerator, A value);

  default @NotNull String encode(final A value) {
    return getUnchecked(() -> {
      var result = new ByteArrayOutputStream();
      try (var generator = new JsonFactory().createGenerator(result)) {
        encode(generator, value);
      }
      return result.toString();
    });
  }

}
