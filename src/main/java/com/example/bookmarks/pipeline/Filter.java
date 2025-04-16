package com.example.bookmarks.pipeline;

@FunctionalInterface
public interface Filter<I, O> {
    O apply(I input);
}
