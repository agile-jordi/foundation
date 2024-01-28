package javautils;

import static javautils.ThrowingSupplier.getUnchecked;

import java.util.function.Function;

public interface ThrowingFunction<T, R> extends Function<T, R> {
  R applyChecked(final T t) throws Exception;

  @Override default R apply(final T t) {
    return getUnchecked(() -> applyChecked(t));
  }

}
