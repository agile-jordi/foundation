package com.agilogy.json;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record Field<A, B>(
      @NotNull String name,
      @NotNull Function<A, B> getter,
      @NotNull JsonCodec<B> codec
) {
  public static <A, B> Field<A, B> field(
        final @NotNull String name,
        final @NotNull Function<A, B> getter,
        final @NotNull JsonCodec<B> codec
  ) {
    return new Field<>(name, getter, codec);
  }

  public static <A> Field<A, String> field(final @NotNull String name, final @NotNull Function<A, String> getter) {
    return new Field<>(name, getter, JsonCodec.string);
  }

  public B get(final @NotNull A a) {
    return getter.apply(a);
  }
}