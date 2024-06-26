package javautils.function;

@FunctionalInterface
public interface Function4<A1,A2,A3,A4,B> {

  B apply(A1 a1, A2 a2, A3 a3, A4 a4);
}