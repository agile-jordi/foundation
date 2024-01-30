package javautils;

import static javautils.ThrowingSupplier.getUnchecked;

@FunctionalInterface
public interface ThrowingRunnable extends Runnable {
  void runChecked() throws Exception;

  @Override
  default void run() {
    runUnchecked(this);
  }

  default @NotNull ThrowingSupplier<Unit> asSupplier(){
    return () -> {
      runChecked();
      return Unit.unit;
    };
  }
  static void runUnchecked(@NotNull final ThrowingRunnable f) {
    getUnchecked(f);
  }

}
