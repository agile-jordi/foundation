package javautils;

import java.util.function.Function;

public interface ThrowingFunction<T, R> extends Function<T, R> {
  R applyOrThrow(T t) throws Exception;

  @Override default R apply(T t) {
    return unchecked().apply(t);
  }

  default Function<T, R> unchecked() {
    return new Function<T, R>() {
      @Override public R apply(T t) {
        try {
          return ThrowingFunction.this.applyOrThrow(t);
        } catch (RuntimeException e) {
          throw e;
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

}
