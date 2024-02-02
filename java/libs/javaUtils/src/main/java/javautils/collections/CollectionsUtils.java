package javautils.collections;

import java.util.HashSet;
import java.util.Set;

public final class CollectionsUtils {
  public static <A> Set<A> diff(Set<A> s1, Set<A> s2) {
    var tmp = new HashSet<A>(s1);
    tmp.removeAll(s2);
    return tmp;
  }
}
