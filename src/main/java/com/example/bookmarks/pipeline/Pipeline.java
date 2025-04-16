package com.example.bookmarks.pipeline;

import java.util.List;

public class Pipeline<I, O> {
  private final List<Filter<?, ?>> filters;

  public Pipeline(List<Filter<?, ?>> filters) {
    this.filters = filters;
  }

  @SuppressWarnings("unchecked")
  public O run(I input) {
    Object result = input;
    for (Filter<?, ?> filter : filters) {
      result = ((Filter<Object, Object>) filter).apply(result);
    }
    return (O) result;
  }
}
