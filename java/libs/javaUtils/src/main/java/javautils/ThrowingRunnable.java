package javautils;

@FunctionalInterface
public interface ThrowingRunnable {
  void run() throws Exception;

  static void runOrThrow(ThrowingRunnable f){
    attempt(f);
  }
  static void attempt(ThrowingRunnable f) {
    try {
      f.run();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
