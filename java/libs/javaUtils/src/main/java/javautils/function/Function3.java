package javautils.function;

@FunctionalInterface
public interface Function3<A1,A2,A3,B> {

  B apply(A1 a1, A2 a2, A3 a3);
}