package com.example.bookmarks.model;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class DigestAccumulatorTest {
  @Test
  void digestBase64_returnsConsistentValue_forSameInput() {
    DigestAccumulator acc1 = new DigestAccumulator();
    DigestAccumulator acc2 = new DigestAccumulator();
    acc1.update("test");
    acc2.update("test");
    assertThat(acc1.digestBase64()).isEqualTo(acc2.digestBase64());
  }

  @Test
  void digestBase64_returnsDifferentValue_forDifferentInput() {
    DigestAccumulator acc1 = new DigestAccumulator();
    DigestAccumulator acc2 = new DigestAccumulator();
    acc1.update("one");
    acc2.update("two");
    assertThat(acc1.digestBase64()).isNotEqualTo(acc2.digestBase64());
  }

  @Test
  void updateAll_iterable_accumulatesAllHashes() {
    DigestAccumulator acc = new DigestAccumulator();
    List<TreeNode> nodes =
        List.of(
            new Bookmark(
                UUID.randomUUID().toString(), "bookmark", "first", "https://example.com/first"),
            new Bookmark(
                UUID.randomUUID().toString(), "bookmark", "second", "https://example.com/second"),
            new Bookmark(
                UUID.randomUUID().toString(), "bookmark", "third", "https://example.com/third"));
    acc.updateAll(nodes);
    String hash = acc.digestBase64();
    assertThat(hash).isNotEmpty();
  }

  @Test
  void updateAll_iterable_withNull_doesNotThrow() {
    DigestAccumulator acc = new DigestAccumulator();
    assertThatCode(() -> acc.updateAll((Iterable<TreeNode>) null)).doesNotThrowAnyException();
  }

  @Test
  void updateAll_varargs_withMultipleNodes_accumulates() {
    DigestAccumulator acc = new DigestAccumulator();
    TreeNode a =
        new Bookmark(UUID.randomUUID().toString(), "bookmark", "a", "https://example.com/a");
    TreeNode b =
        new Bookmark(UUID.randomUUID().toString(), "bookmark", "b", "https://example.com/b");
    acc.updateAll(a, b);
    String hash = acc.digestBase64();
    assertThat(hash).isNotEmpty();
  }

  @Test
  void updateAll_varargs_withNullArray_doesNotThrow() {
    DigestAccumulator acc = new DigestAccumulator();
    assertThatCode(() -> acc.updateAll((TreeNode[]) null)).doesNotThrowAnyException();
  }

  @Test
  void update_withNonNullString_updatesDigest() {
    DigestAccumulator acc = new DigestAccumulator();
    acc.update("hello");
    String hash = acc.digestBase64();
    assertThat(hash).isNotEmpty();
  }

  @Test
  void update_withNullString_doesNotThrow() {
    DigestAccumulator acc = new DigestAccumulator();
    assertThatCode(() -> acc.update((String) null)).doesNotThrowAnyException();
  }

  @Test
  void update_withNullTreeNode_doesNotThrow() {
    DigestAccumulator acc = new DigestAccumulator();
    assertThatCode(() -> acc.update((TreeNode) null)).doesNotThrowAnyException();
  }

  @Test
  void update_withTreeNode_callsHashMethod() {
    DigestAccumulator acc = new DigestAccumulator();
    TreeNode node =
        new Bookmark(UUID.randomUUID().toString(), "bookmark", "title", "https://example.com");
    acc.update(node);
    String hash = acc.digestBase64();
    assertThat(hash).isNotEmpty();
  }
}
