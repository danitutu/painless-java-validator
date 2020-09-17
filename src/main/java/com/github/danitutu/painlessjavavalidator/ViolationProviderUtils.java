package com.github.danitutu.painlessjavavalidator;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class ViolationProviderUtils {
  private ViolationProviderUtils() {
  }

  /**
   * Creates {@link ViolationProvider}s from collection using a mapping function. The mapping
   * function receives a pair of <code>(i, o)</code> where <code>i</code> is the index of current
   * iterated element and <code>o</code> is the iterated element. The function must return a {@link
   * ViolationProvider}.
   *
   * @param list   collection to be mapped
   * @param mapper mapping function
   * @param <T>    type of elements from collection
   * @return list of {@link ViolationProvider}
   */
  public static <T> List<ViolationProvider> from(
          List<T> list, BiFunction<Integer, T, ViolationProvider> mapper) {
    if (mapper == null) {
      throw new IllegalArgumentException("mapper is required");
    }
    if (list == null) {
      return emptyList();
    }
    return IntStream.range(0, list.size())
            .mapToObj(i -> mapper.apply(i, list.get(i)))
            .collect(toList());
  }
}
