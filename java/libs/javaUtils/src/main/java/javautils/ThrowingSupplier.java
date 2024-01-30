package javautils;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T> extends Supplier<T> {
  T getChecked() throws Exception;

  @Override default T get() {
    return getUnchecked(this);
  }

  static Unit getUnchecked(@NotNull final ThrowingRunnable f) {
    return getUnchecked(f.asSupplier());
  }

  static <A> A getUnchecked(@NotNull final ThrowingSupplier<A> f) {
    try {
      return f.getChecked();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
