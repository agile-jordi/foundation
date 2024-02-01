package com.agilogy.json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public sealed interface Json {

  @SafeVarargs static @NotNull Json.Object jsonObj(@NotNull Map.Entry<java.lang.String, Json>... pairs) {
    return new Json.Object(pairs);
  }

  static @NotNull Json.Array jsonArr(@NotNull Json... elements) {
    return new Json.Array(elements);
  }

  static @NotNull Json.Number json(int value) {
    return new Json.Number(value);
  }

  static @NotNull Json.Number json(long value) {
    return new Json.Number(value);
  }

  static @NotNull Json.Number json(double value) {
    return new Json.Number(value);
  }

  static @NotNull Json.Boolean json(boolean value) {
    return new Json.Boolean(value);
  }

  static @NotNull Json.String json(java.lang.String value) {
    return new Json.String(value);
  }

  default @NotNull java.lang.String asString() {
    return ((JsonEncoder<Json>) JsonCodec::encodeJson).encode(this);
  }

  final class Object implements Json, Iterable<Map.Entry<java.lang.String, Json>> {
    private final @NotNull Map<java.lang.String, Json> map;

    @SafeVarargs public Object(@NotNull Map.Entry<java.lang.String, Json>... pairs) {
      this.map = Map.ofEntries(pairs);
    }

    public Object(@NotNull Map<java.lang.String, Json> values) {
      this.map = Map.copyOf(values);
    }

    public @Nullable Json get(@NotNull java.lang.String key) {
      return map.get(key);
    }

    public @NotNull Set<Map.Entry<java.lang.String, Json>> entrySet() {
      return map.entrySet();
    }

    @Override public @NotNull Iterator<Map.Entry<java.lang.String, Json>> iterator() {
      return entrySet().iterator();
    }

    @Override public @NotNull java.lang.String toString() {
      return asString();
    }

    @Override public boolean equals(java.lang.Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Object obj = (Object) o;
      return Objects.equals(map, obj.map);
    }

    @Override public int hashCode() {
      return Objects.hash(map);
    }
  }

  final class Array implements Json, Iterable<Json> {
    public final @NotNull List<Json> list;

    public Array(@NotNull Json... values) {
      this.list = List.of(values);
    }

    public Array(@NotNull List<Json> values) {
      this.list = List.copyOf(values);
    }

    public @Nullable Json get(int index) {
      return list.get(index);
    }

    @Override public @NotNull Iterator<Json> iterator() {
      return list.iterator();
    }

    @Override public boolean equals(java.lang.Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Array arr = (Array) o;
      return Objects.equals(list, arr.list);
    }

    @Override public int hashCode() {
      return Objects.hash(list);
    }

    @Override public @NotNull java.lang.String toString() {
      return asString();
    }
  }

  record Number(@NotNull java.lang.String value) implements Json {

    public Number(int value) {
      this(Integer.toString(value));
    }

    public Number(long value) {
      this(Long.toString(value));
    }

    public Number(double value) {
      this(Double.toString(value));
    }

    @Override public @NotNull java.lang.String toString() {
      return asString();
    }
  }

  record Boolean(boolean value) implements Json {
    public static final Boolean TRUE = new Boolean(true);
    public static final Boolean FALSE = new Boolean(false);

    @Override public @NotNull java.lang.String toString() {
      return asString();
    }
  }

  record String(@NotNull java.lang.String value) implements Json {
    @Override public @NotNull java.lang.String toString() {
      return asString();
    }
  }
}
