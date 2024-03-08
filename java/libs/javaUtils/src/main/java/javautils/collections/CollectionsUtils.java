package javautils.collections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CollectionsUtils {

  public static <A> Set<A> diff(Set<A> s1, Set<A> s2) {
    var tmp = new HashSet<A>(s1);
    tmp.removeAll(s2);
    return tmp;
  }

  public static <A> List<A> join(final List<A> list1, final List<A> list2) {
    final var res = new ArrayList<>(list1);
    res.addAll(list2);
    return res;
  }

  public static <A> Set<A> union(final Set<A> set1, final Set<A> set2) {
    final var res = new HashSet<>(set1);
    res.addAll(set2);
    return res;
  }

  public static <A> Set<A> plus(final Set<A> set1, final A elem) {
    final var res = new HashSet<>(set1);
    res.add(elem);
    return res;
  }

  public static <A> Set<A> minus(final Set<A> set1, final Set<A> elem) {
    final var res = new HashSet<>(set1);
    res.removeAll(elem);
    return res;
  }
}
