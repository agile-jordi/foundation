package javautils;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T>, ThrowingFunction<T, Unit> {
  void acceptChecked(T t) throws Exception;

  @Override default Unit applyChecked(final T t) throws Exception{
    acceptChecked(t);
    return Unit.unit;
  }

  @Override default void accept(T t) {
    apply(t);
  }
}
