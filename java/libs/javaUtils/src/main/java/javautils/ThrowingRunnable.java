package javautils;

@FunctionalInterface
public interface ThrowingRunnable extends Runnable {
  void runChecked() throws Exception;

  @Override
  default void run() {
    runUnchecked(this);
  }

  static void runUnchecked(@NotNull final ThrowingRunnable f) {
    try {
      f.runChecked();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
