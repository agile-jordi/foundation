package com.agilogy.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import static javautils.ThrowingSupplier.getUnchecked;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JsonCodec {

  public static Json parse(String s) {
    return getUnchecked(() -> {
      var parser = new JsonFactory().createParser(s);
      return parse(parser);
    });
  }
  public static Json parse(InputStream is) {
    return getUnchecked(() -> {
      var parser = new JsonFactory().createParser(is);
      return parse(parser);
    });
  }

  private static Json parse(JsonParser p) throws IOException {
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
          values.add(parse(p));
        }
        return new Json.Array(values);
      }
      case JsonTokenId.ID_START_OBJECT: {
        Map<String, Json> values = new HashMap<>();
        while (p.nextToken() != JsonToken.END_OBJECT) {
          final String currentName = p.getCurrentName();
          p.nextToken();
          values.put(currentName, parse(p));
        }
        return new Json.Object(values);
      }
      case JsonTokenId.ID_NULL:
        return null;
      default:
    }
    throw new UnsupportedOperationException("Unsupported token id " + tokenId + " (" + p.currentToken() + ")");

  }

  public static String serialize(Json json) {
    return getUnchecked(() -> {
      var result = new ByteArrayOutputStream();
      try(var generator = new JsonFactory().createGenerator(result)){
        serialize(generator, json);
      }
      return result.toString();
    });
  }

  public static void serialize(JsonGenerator g, Json json) throws IOException {
    switch (json) {
      case Json.Array array -> {
        g.writeStartArray();
        for (Json value : array) serialize(g, value);
        g.writeEndArray();
      }
      case Json.Boolean aBoolean -> g.writeBoolean(aBoolean.value());
      case Json.Number number -> g.writeNumber(number.value());
      case Json.Object object -> {
        g.writeStartObject();
        for (Map.Entry<String, Json> entry : object) {
          g.writeFieldName(entry.getKey());
          serialize(g, entry.getValue());
        }
        g.writeEndObject();
      }
      case Json.String string -> g.writeString(string.value());
    }

  }

}
