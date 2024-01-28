package javautils;

@FunctionalInterface
public interface ThrowingSupplier<T> {
  T get() throws Exception;
  default T unsafeGet(){
    return getOrThrow(this);
  }

  static <A> A getOrThrow(ThrowingSupplier<A> f) {
    return attempt(f);
  }

  static <A> A attempt(ThrowingSupplier<A> f) {
    try {
      return f.get();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
