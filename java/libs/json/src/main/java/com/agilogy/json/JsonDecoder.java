package com.agilogy.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import static javautils.ThrowingSupplier.getUnchecked;

import java.io.InputStream;

@FunctionalInterface
public interface JsonDecoder<A> {
  A decode(JsonParser parser);

  default A decode(InputStream is) {
    return getUnchecked(() -> {
      var parser = new JsonFactory().createParser(is);
      return decode(parser);
    });
  }

  default A decode(String s) {
    return getUnchecked(() -> {
      var parser = new JsonFactory().createParser(s);
      return decode(parser);
    });
  }

}
